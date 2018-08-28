#!/bin/sh

PRODUCT_BASENAME=unicastnotifierservice
PRODUCT_VARIANT=

VARIANT_STRING=`[ -z $PRODUCT_VARIANT ] || echo ".$PRODUCT_VARIANT"`
PRODUCT_NAME=$PRODUCT_BASENAME$VARIANT_STRING
PRODUCT_GROUP_NAME=avs-iptv
PRODUCT_SUBGROUP_NAME=business-services
PRODUCT_VER=product.dev


PRODUCT_SUB_SUBGROUP_NAME=unicast-notifier-service
CHANGENUM_VERSIONING=N

JAVA_VER=1.8.0_91
JAVA_DIR=j2sdk$JAVA_VER
MAVEN_VER=3.0.4
MAVEN_DIR=maven$MAVEN_VER

SUDO=sudo

COMPONENT_SERIES=$PRODUCT_NAME-$PRODUCT_VER

BASE_DIR=../$PRODUCT_NAME
BUILD_DIR=$BASE_DIR/target
OUTPUT_STAGING_DIR=${BUILD_DIR}/staging
STAGING_DIR=/mnt/staging/builds.acn/$PRODUCT_VER/$PRODUCT_GROUP_NAME/$PRODUCT_SUBGROUP_NAME/$PRODUCT_SUB_SUBGROUP_NAME
REPORTS_DIR=/mnt/scm/reports/diff/$PRODUCT_VER/$PRODUCT_NAME
QASYNCSCRIPT_DIR=$BASE_DIR/../qa/scm
OUTPUT_DIR=`pwd`/Package

DEPLOYMENT_PATH=/usr/local/java/$PRODUCT_NAME

source /home/build/scm/$PRODUCT_VER/common/scm/builds.common.sh
source /home/build/scm/$PRODUCT_VER/common/scm/perforce.common.sh

do_version_file () {
    # Create version file
    echo 'Create version file...'
    VERFILE=$PRODUCT_NAME.version
    rm -f $VERFILE 2> /dev/null
    touch $VERFILE; chmod 755 $VERFILE
    echo '#' > $VERFILE
    echo '# Myrio Install Version file format 0.2' >> $VERFILE
    echo '#' >> $VERFILE
    echo "VERSION=$BUILDFILE_NAME" >> $VERFILE
    echo "PRODUCT_NAME=$PRODUCT_NAME" >> $VERFILE
    $SUDO cp -a $VERFILE $TARGET_DIR/.
}

build_prep () {
    echo -n 'Checking Java version... '
    CURR_JAVA_VER=`java -version 2>&1 | awk ' NF==3 { split($3,x,"\""); print x[2]; }'`
    if [ "$CURR_JAVA_VER" != "$JAVA_VER" ]
    then
      # Setting Java to correct version
      echo "currently $CURR_JAVA_VER -- setting to $JAVA_VER"
      (
        cd /usr
        if [ ! -d $JAVA_DIR ]
        then
          ERROR_STR="Correct Java SDK directory $JAVA_DIR not found!"
          RETVAL=1
          return
        else
          $SUDO rm -f java
          $SUDO ln -sf $JAVA_DIR java
        fi
      )
      if [ $? -gt 0 ]
      then
        ERROR_STR="Error setting Java version!"
        RETVAL=1
        return
      fi
    else
      echo " $CURR_JAVA_VER -- correct!"
    fi

    echo -n 'Checking Maven version... '
    CURR_MAVEN_VER=`mvn -version 2>&1 | awk '{ print $3 }' | head -1`
    if [ "$CURR_MAVEN_VER" != "$MAVEN_VER" ]
    then
      # Setting Maven to correct version
      echo "currently $CURR_MAVEN_VER -- setting to $MAVEN_VER"
      (
        cd /usr
        if [ ! -d $MAVEN_DIR ]
        then
          ERROR_STR="Correct Maven directory $MAVEN_DIR not found!"
          RETVAL=1
          return
        else
          $SUDO rm -f maven
          $SUDO ln -sf $MAVEN_DIR maven
        fi
      )
      if [ $? -gt 0 ]
      then
        ERROR_STR="Error setting Maven version!"
        RETVAL=1
        return
      fi
    else
      echo " $CURR_MAVEN_VER -- correct!"
    fi

}

build () {
    RETVAL=0

    echo ''
    echo '----------------------------------------------------------------'
    echo " Pre-Build cleanup for $CURRBUILD_NAME"
    echo '----------------------------------------------------------------'
    echo ''
    $SUDO rm -f $BASE_DIR/*.log
    $SUDO rm -rf $BASE_DIR/pkg
    $SUDO rm -f $OUTPUT_DIR/*.tar.gz
    mkdir -p $OUTPUT_DIR

    echo ''
    echo '----------------------------------------------------------------'
    echo " Building $CURRBUILD_NAME"
    echo '----------------------------------------------------------------'
    echo ''

    cd $BASE_DIR

    #export JBOSS_HOME=$BASE_DIR/jboss
    #echo $JBOSS_HOME
    mvn clean install  > mavenbuild.log 2>&1
    if [ $? -gt 0 ]
    then
      ERROR_STR="ERROR Building configuration-service!"
      RETVAL=1
      return
    fi
    do_version_file
}

do_tar_file () {

    PKGDIR=`pwd`
    RETVAL=0
  
    do_version_file

    $SUDO rm -rf $DEPLOYMENT_PATH 2> /dev/null
    $SUDO mkdir -p $DEPLOYMENT_PATH 2> /dev/null

    $SUDO mkdir -p $DEPLOYMENT_PATH 2> /dev/null
    pwd

    $SUDO cp $PKGDIR/target/unicast-notifier-service.jar  $DEPLOYMENT_PATH/.
    $SUDO cp ./$VERFILE $DEPLOYMENT_PATH/.

    # Create tar file
    echo "Creating $BUILDFILE_NAME.tar.gz..."
    $SUDO rm $BUILDFILE_NAME.tar.gz
    dir=`pwd`
    cd $DEPLOYMENT_PATH
    $SUDO tar czfp $OUTPUT_DIR/$BUILDFILE_NAME.tar.gz .
    cd $dir
}

finish () {
    RETVAL=0
    echo "Copying tar file to $STAGING_DIR/$BASE_SWVERSION..."
    # Coping build output to staging area buffer
    mkdir -p $STAGING_DIR
    sudo cp -a $OUTPUT_DIR/* $STAGING_DIR

    deploy
}

build_prep
  if [ $RETVAL -gt 0 ]; then build_error $ERROR_STR ; fi

build
  if [ $RETVAL -gt 0 ]; then build_error $ERROR_STR ; fi

do_tar_file
  if [ $RETVAL -gt 0 ]; then build_error $ERROR_STR ; fi

finish 
echo 'BUILD COMPLETED!'



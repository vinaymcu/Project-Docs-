package com.acn.avs.unicast.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acn.avs.unicast.config.ConfigurationProperties;
import com.acn.avs.unicast.entity.Notifications;
import com.acn.avs.unicast.entity.SetTopBoxes;
import com.acn.avs.unicast.entity.TriggerMapping;
import com.acn.avs.unicast.event.json.UpdateEventTrigger;
import com.acn.avs.unicast.event.param.StbNotificationParam;
import com.acn.avs.unicast.repository.StbManagerRepository;
import com.acn.avs.unicast.repository.UnicastTriggerMappingRepository;
import com.acn.avs.unicast.service.StbNotifierService;
import com.google.gson.Gson;

/**
 * 
 * @author Anand.Jha
 * 
 * This class is responsible to notify the trigger to STB via its corresponding protocol
 * 
 * Currently it having the supports only for UDP and TCP 
 *
 */
@Service
public class StbNotifierServiceImpl implements StbNotifierService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StbNotifierService.class);
	
	@Autowired
	StbManagerRepository stbManagerRepository;
	
	@Autowired
	UnicastTriggerMappingRepository unicastTriggerMappingRepository;
	
	@Autowired
	ConfigurationProperties configurationProperties;
	
	@Autowired
	Gson gson;
	
	/**
	 * TCP PROTOCOL
	 */
	private static final String TCPPROTOCOL = "TCP";
	
	/**
	 * UDP PROTOCOL
	 */
	private static final String UDPPROTOCOL = "UDP";
	
	/**
	 * MESSAGE INFO
	 */
	private static final String MESSAGE_INFO = "S_MESSAGE_INFO"; 
	
	/**
	 * inetConnectionMode
	 */
	private static int inetConnectionMode;
	
	/**
	 * 
	 */
	@PostConstruct
	private void initializeInetConnection(){
		inetConnectionMode = getConnectionIdForImplicitNAT();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Implicit Connection Mode ID: "+ inetConnectionMode );
		}
	}
	/**
	 * Send an update on a new socket
	 * 
	 * @param macAddress
	 * 
	 * @param evt
	 *            the update event to send
	 * @param host
	 *            the host to send to
	 * @param port
	 *            the port on which the UpdateDaemon is listening
	 */
	private void notify(String macAddress, String evt, String host, int port, String protocol) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("----------- Sending Notification request to STB -------------");
			LOGGER.info("MacAddress : "+ macAddress+" Host: " + host + " Port: " + port +" Protocol: "+ protocol  );
		}

		try {
			SocketAddress address = new InetSocketAddress(host, port);

			if (TCPPROTOCOL.equals(protocol)) {
				notifyViaTCP(evt, address);
			} else if (UDPPROTOCOL.equals(protocol)){
				notifyViaUDP(evt, address);
			} else{
				LOGGER.error("Notification for macAddress : "+ macAddress+" could not sent due to protocol: "+ protocol + " won't supported.");
			}

		} catch (IOException ioException) {
			LOGGER.error("Error occured while sending notification for macAddress : "+ macAddress, ioException);
		}
		

	}

	/**
	 * @param stbNotificationParam
	 * 
	 * @return
	 */
	@Override
	public void notify(StbNotificationParam stbNotificationParam) {
		notify(stbNotificationParam.getMacAddress(), stbNotificationParam.getUpdateEvent(), stbNotificationParam.getStbHost(),
				stbNotificationParam.getStbPort(), stbNotificationParam.getStbProtocol());
	}

	/**
	 * Send notification via UDP using DatagramSocket
	 * 
	 * @param evt
	 * @param address
	 * 
	 * @throws IOException
	 */
	private void notifyViaUDP(String evt, SocketAddress address) throws IOException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Sending trigger update through UDP." );
		}
		byte[] byteArray;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(evt);
		byteArray = bos.toByteArray();

		DatagramSocket ds = new DatagramSocket();
		DatagramPacket dp = new DatagramPacket(byteArray, byteArray.length, address);
		ds.send(dp);
		ds.close();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("trigger update sent through UDP!!! ");
		}

	}

	/**
	 * Send notification via TCP using Socket
	 * 
	 * @param evt
	 * @param address
	 * 
	 * @throws IOException
	 */
	private void notifyViaTCP(String evt, SocketAddress address) throws IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Sending trigger update through TCP." );
		}
		
		Socket socket = new Socket();
		socket.connect(address, configurationProperties.getStbNotificationTimeout());
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(evt);
		out.close();
		socket.close();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("trigger update sent through TCP!!! ");
		}

	}

	/**
	 * Get All SetTopBoxes for corresponding subscriberId
	 * 
	 * @param subscriberId
	 * 
	 * @return List
	 */
	@Override
	public List<SetTopBoxes> getSetTopBoxes(String subscriberId) {
		return stbManagerRepository.getSetTopBoxes(subscriberId);
	}

	
	
	/**
	 * Get SetTopBox with protocol and port detail
	 * 
	 * @param equipmentId
	 * @param serviceName
	 * 
	 * @return SetTopBoxes
	 */
	@Override
	public SetTopBoxes getSetTopBoxPortAndProtocol(int equipmentId, String serviceName){
		return stbManagerRepository.getSetTopBoxPortAndProtocol(equipmentId, serviceName);
	}
	
	
	/**
	 * Get Json from UpdateEvent 
	 * 
	 * @param triggerType
	 * @param triggerInfo
	 * 
	 * @return String
	 */
	@Override
	public String getUpdateEventAsJson(String triggerType, String triggerInfo){
		
		UpdateEventTrigger updateEventTrigger = new UpdateEventTrigger(triggerType, triggerInfo);
		String updateEventTriggerAsJson =  gson.toJson(updateEventTrigger);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("updateEventTriggerAsJson: "+ updateEventTriggerAsJson);
		}
		
		return updateEventTriggerAsJson;
	}
	
	
	/**
	 * Get serviceName as per triggerType
	 * 
	 * @param triggerType
	 * @return String
	 */
	@Override
	public String getServiceNameByTriggerType(String triggerType){
		return this.getTriggerMapping(triggerType).getServiceName();
	}
	
	/**
	 * Get triggerMapping as per trigger type
	 * 
	 * @param triggerType
	 * @return TriggerMapping
	 */
	@Override
	public TriggerMapping getTriggerMapping(String triggerType){
		return  unicastTriggerMappingRepository.findByTriggerType(triggerType);
	}
	
	/**
	 * 
	 * Get All notifications 
	 * 
	 * @param subscriberId
	 * 
	 * @return Notifications
	 */
	@Override
	public Notifications getNotifications(String subscriberId){
		
		List<SetTopBoxes> setTopBoxes = stbManagerRepository.getSetTopBoxes(subscriberId);
		
		return this.getNotifications(setTopBoxes);
		
	}
	
	/**
	 * 
	 * Get notifications as per sourceMacAddress
	 * 
	 * @param subscriberId
	 * @param sourceMacAddress
	 * 
	 * @return Notifications
	 */
	@Override
	public Notifications getNotifications(String subscriberId, String sourceMacAddress){
		
		List<SetTopBoxes> setTopBoxes = stbManagerRepository.getSetTopBoxes(subscriberId, sourceMacAddress);
		
		return this.getNotifications(setTopBoxes);
		
	}
	
	/**
	 * 
	 * get notifications as per targetMacAddress
	 * 
	 * @param subscriberId
	 * @param targetMacAddress
	 * 
	 * @return Notifications
	 */
	@Override
	public Notifications getNotifications(String subscriberId, List<String> targetMacAddress){
		
		List<SetTopBoxes> setTopBoxes = stbManagerRepository.getSetTopBoxes(subscriberId, targetMacAddress);
		
		return this.getNotifications(setTopBoxes);
		
	}
	
	/**
	 * get notifications as per connection mode
	 * 
	 * @param setTopBoxes
	 * @return
	 */
	private Notifications getNotifications(List<SetTopBoxes> setTopBoxes){
		List<SetTopBoxes> tqsNotifications = new ArrayList<SetTopBoxes>();
		List<SetTopBoxes> stbNotifications = new ArrayList<SetTopBoxes>();
		
		Notifications notifications = new Notifications(tqsNotifications, stbNotifications);
		
		for (SetTopBoxes setTopBox: setTopBoxes){
		if (isImplicitNATMode (setTopBox.getConnectionMode())){
				tqsNotifications.add(setTopBox);
			}else {
				stbNotifications.add(setTopBox);
			}
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("tqsNotifications : "+ tqsNotifications.size() + ", stbNotifications :"+ stbNotifications.size());
		}
		return notifications;
	}
	
	/**
	 * Returns true if specified mode is Implicit NAT.
	 * 
	 * @param connectionMode
	 * @return boolean
	 */
	@Override
	public boolean isImplicitNATMode(int connectionMode) {
		return connectionMode == inetConnectionMode;
	}
	
	
	/**
	 * Check MessageInfo trigger 
	 * 
	 * @param triggerType
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isMessageInfoTrigger(String triggerType) {
		return triggerType.equalsIgnoreCase(MESSAGE_INFO);
	}
	
	/**
	 * Check HwVerion is assciate with service name
	 * 
	 * @param hwVersion
	 * @param serviceName
	 * @return
	 */
	@Override
	public boolean isHwVerionAssciateWithServiceName(String hwVersion, String serviceName){
		return stbManagerRepository.isHwVerionAssciateWithServiceName(hwVersion, serviceName);
	}
	
	/**
	 * @return int
	 */
	@Override
	public int getConnectionIdForImplicitNAT(){
		return stbManagerRepository.getConnectionIdForImplicitNAT();
	}
}

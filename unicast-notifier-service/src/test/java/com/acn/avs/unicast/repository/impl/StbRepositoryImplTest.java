package com.acn.avs.unicast.repository.impl;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.acn.avs.unicast.config.TestConfig;
import com.acn.avs.unicast.entity.SetTopBoxes;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestConfig.class })
@WebAppConfiguration
public class StbRepositoryImplTest {
	@Autowired
	@Qualifier("stbmanagerJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	StbManagerRepositoryImpl stbManagerRepositoryImpl;
	protected MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * test getSetTopBoxes with valid subscriber id
	 */
	@Test
	public void testGetSetTopBoxes() {
		settingDefaults();
		List<SetTopBoxes> setTopBoxesList = stbManagerRepositoryImpl.getSetTopBoxes("156987");
		assertEquals(1, setTopBoxesList.size());
	}

	/**
	 * test getSetTopBoxes with invalid subscriber id
	 */
	@Test
	public void testGetSetTopBoxes2() {
		settingDefaults();
		List<SetTopBoxes> setTopBoxesList = stbManagerRepositoryImpl.getSetTopBoxes("0");
		assertEquals(setTopBoxesList.size(), 0);
	}

	/**
	 * testing getSetTopBoxes with existing subs id and mac
	 */
	@Test
	public void testGetSetTopBoxWithSourceMacAddress() {
		settingDefaults();
		List<SetTopBoxes> setTopBoxList = stbManagerRepositoryImpl.getSetTopBoxes("102365", "ASB47EF");
		assertEquals(3, setTopBoxList.size());
	}

	/**
	 * testing getSetTopBoxes with existing subs id but mac does not exist
	 */
	@Test
	public void testGetSetTopBoxWithSourceMacAddress2() {
		settingDefaults();
		List<SetTopBoxes> setTopBoxList = stbManagerRepositoryImpl.getSetTopBoxes("102365", "MOTOROLA");
		assertEquals(4, setTopBoxList.size());
	}

	/**
	 * testing getSetTopBoxes with existing subs id and mac but mac is null
	 */
	@Test
	public void testGetSetTopBoxWithSourceMacAddress3() {
		settingDefaults();
		List<SetTopBoxes> setTopBoxList = stbManagerRepositoryImpl.getSetTopBoxes("102365", "");
		assertEquals(4, setTopBoxList.size());
	}

	/**
	 * testing getSetTopBoxes with invalid subs id
	 */
	@Test
	public void testGetSetTopBoxWithSourceMacAddress4() {
		settingDefaults();
		List<SetTopBoxes> setTopBoxList = stbManagerRepositoryImpl.getSetTopBoxes("0", "ASB47EF");
		assertEquals(0, setTopBoxList.size());
	}

	/**
	 * testing getSetTopBoxes with invalid subs id
	 */
	@Test
	public void testGetSetTopBoxWithSourceMacAddress5() {
		settingDefaults();
		List<SetTopBoxes> setTopBoxList = stbManagerRepositoryImpl.getSetTopBoxes("0", "MOTOROLA");
		assertEquals(0, setTopBoxList.size());
	}

	/**
	 * testing getSetTopBoxes with invalid subs id
	 */
	@Test
	public void testGetSetTopBoxWithSourceMacAddress6() {
		settingDefaults();
		List<SetTopBoxes> setTopBoxList = stbManagerRepositoryImpl.getSetTopBoxes("0", "");
		assertEquals(0, setTopBoxList.size());
	}

	/**
	 * test serviceIdHardwareValidate with valid input
	 */
	@Test
	public void testServiceIdHardwareValidate() {
		settingDefaults();
		boolean flag = stbManagerRepositoryImpl.isHwVerionAssciateWithServiceName("MOTOROLA", "STB Trigger Update");
		assertEquals(true, flag);
	}

	/**
	 * test serviceIdHardwareValidate with invalid input
	 */
	@Test
	public void testServiceIdHardwareValidate2() {
		settingDefaults();
		boolean flag = stbManagerRepositoryImpl.isHwVerionAssciateWithServiceName("1017", "Rest");
		assertEquals(false, flag);
	}

	/**
	 * test serviceIdHardwareValidate with invalid input
	 */
	@Test
	public void testServiceIdHardwareValidate3() {
		settingDefaults();
		boolean flag = stbManagerRepositoryImpl.isHwVerionAssciateWithServiceName("1017", null);
		assertEquals(false, flag);
	}

	/**
	 * test serviceIdHardwareValidate with invalid input
	 */
	@Test
	public void testServiceIdHardwareValidate4() {
		settingDefaults();
		boolean flag = stbManagerRepositoryImpl.isHwVerionAssciateWithServiceName("0", "TEST");
		assertEquals(false, flag);
	}

	/**
	 * test serviceIdHardwareValidate with invalid input
	 */
	@Test
	public void testServiceIdHardwareValidate5() {
		settingDefaults();
		boolean flag = stbManagerRepositoryImpl.isHwVerionAssciateWithServiceName("0", "Rest");
		assertEquals(false, flag);
	}

	/**
	 * test serviceIdHardwareValidate with invalid input
	 */
	@Test
	public void testServiceIdHardwareValidate6() {
		settingDefaults();
		boolean flag = stbManagerRepositoryImpl.isHwVerionAssciateWithServiceName("0", "TEST");
		assertEquals(false, flag);
	}

	/**
	 * test serviceIdHardwareValidate with invalid input
	 */
	@Test
	public void testServiceIdHardwareValidate7() {
		settingDefaults();
		boolean flag = stbManagerRepositoryImpl.isHwVerionAssciateWithServiceName(null, "TEST");
		assertEquals(false, flag);
	}

	/**
	 * test serviceIdHardwareValidate with invalid input
	 */
	@Test
	public void testServiceIdHardwareValidate8() {
		settingDefaults();
		boolean flag = stbManagerRepositoryImpl.isHwVerionAssciateWithServiceName(null, "Rest");
		assertEquals(false, flag);
	}

	/**
	 * test serviceIdHardwareValidate with invalid input
	 */
	@Test
	public void testServiceIdHardwareValidate9() {
		settingDefaults();
		boolean flag = stbManagerRepositoryImpl.isHwVerionAssciateWithServiceName(null, "TEST");
		assertEquals(false, flag);
	}

	/**
	 * test GetSetTopBoxPortAndProtocol with valid input
	 */
	@Test
	public void testGetSetTopBoxPortAndProtocol() {
		settingDefaults();
		SetTopBoxes setTopBoxes = stbManagerRepositoryImpl.getSetTopBoxPortAndProtocol(1017, "STB Trigger Update");
		assertEquals(9001, setTopBoxes.getExternalPort());
	}

	/**
	 * test GetSetTopBoxPortAndProtocol with invalid input
	 */
	@Test
	public void testGetSetTopBoxPortAndProtocol2() {
		settingDefaults();
		SetTopBoxes setTopBoxes = stbManagerRepositoryImpl.getSetTopBoxPortAndProtocol(1012, "STB Trigger Update");
		assertEquals(8090, setTopBoxes.getExternalPort());
	}

	@Test
	public void testConnectionIdForImplicitNAT() throws NoSuchFieldException, IllegalAccessException {
		settingDefaults();
		setFinalStatic(StbManagerRepositoryImpl.class, "IMPLICIT_NAT_QUERY", "select ID from CONNECTION_MODES where CONNECTION_MODE = 'Implicit NAT'");
		assertEquals(stbManagerRepositoryImpl.getConnectionIdForImplicitNAT(),2);
	}
	
	/**
	 * testing getSetTopBoxes with existing subs id and mac
	 */
	@Test
	public void testGetSetTopBoxesWithMacList() {
		settingDefaults();
		List<String> macList=new ArrayList<>();
		macList.add("ASB47EF");
		List<SetTopBoxes> setTopBoxList = stbManagerRepositoryImpl.getSetTopBoxes("102365", macList);
		assertEquals(1, setTopBoxList.size());
	}

	/**
	 * testing getSetTopBoxes with existing subs id but mac does not exist
	 */
	@Test
	public void testGetSetTopBoxesWithMacList2() {
		settingDefaults();
		List<String> macList=new ArrayList<>();
		macList.add("MOTOROLA");
		List<SetTopBoxes> setTopBoxList = stbManagerRepositoryImpl.getSetTopBoxes("102365", macList);
		assertEquals(0, setTopBoxList.size());
	}

	/**
	 * testing getSetTopBoxes with existing subs id and mac but mac is null
	 */
	@Test
	public void testGetSetTopBoxesWithMacList3() {
		settingDefaults();
		List<String> macList=new ArrayList<>();
		macList.add("");
		List<SetTopBoxes> setTopBoxList = stbManagerRepositoryImpl.getSetTopBoxes("102365", macList);
		assertEquals(0, setTopBoxList.size());
	}

	/**
	 * setting default values
	 */
	private void settingDefaults() {
		try {
			Field field = StbManagerRepositoryImpl.class.getDeclaredField("jdbcTemplate");
			field.setAccessible(true);
			field.set(stbManagerRepositoryImpl, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void setFinalStatic(Class clazz, String fieldName, Object newValue)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		Field modifiers = field.getClass().getDeclaredField("modifiers");
		modifiers.setAccessible(true);
		modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}
}

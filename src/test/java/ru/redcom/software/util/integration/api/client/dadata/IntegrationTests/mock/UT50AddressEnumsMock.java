/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;
import ru.redcom.software.util.integration.api.client.dadata.dto.Address;
import ru.redcom.software.util.integration.api.client.dadata.types.*;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccess.successTest;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT50AddressEnumsMock {
	private static final String URI = "/clean/address";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;

	enum SampleAddress {
		ENUMS_OMITTED("enums omitted",
		              "[{\"source\":\"enums omitted\"}]",
		              UT50AddressEnumsMock::matcherOmitted),
		ENUMS_NULL("enums null",
		           "[{\"source\":\"enums null\",\"fias_level\":null,\"fias_actuality_state\":null,\"capital_marker\":null,\"beltway_hit\":null,\"qc_geo\":null,\"qc_complete\":null,\"qc_house\":null,\"qc\":null}]",
		           UT50AddressEnumsMock::matcherNull),
		ENUMS_EMPTY("enums empty",
		            "[{\"source\":\"enums empty\",\"fias_level\":\"\",\"fias_actuality_state\":\"\",\"capital_marker\":\"\",\"beltway_hit\":\"\",\"qc_geo\":\"\",\"qc_complete\":\"\",\"qc_house\":\"\",\"qc\":\"\"}]",
		            UT50AddressEnumsMock::matcherEmpty),
		ENUMS_SET1("enums set 1",
		           "[{\"source\":\"enums set 1\",\"fias_level\":\"0\",\"fias_actuality_state\":\"0\",\"capital_marker\":\"1\",\"beltway_hit\":\"IN_MKAD\",\"qc_geo\":0,\"qc_complete\":0,\"qc_house\":2,\"qc\":0}]",
		           UT50AddressEnumsMock::matcherSet1),
		ENUMS_SET2("enums set 2",
		           "[{\"source\":\"enums set 2\",\"fias_level\":\"1\",\"fias_actuality_state\":\"1\",\"capital_marker\":\"2\",\"beltway_hit\":\"OUT_MKAD\",\"qc_geo\":1,\"qc_complete\":1,\"qc_house\":3,\"qc\":1}]",
		           UT50AddressEnumsMock::matcherSet2),
		ENUMS_SET3("enums set 3",
		           "[{\"source\":\"enums set 3\",\"fias_level\":\"3\",\"fias_actuality_state\":\"50\",\"capital_marker\":\"3\",\"beltway_hit\":\"IN_KAD\",\"qc_geo\":2,\"qc_complete\":2,\"qc_house\":4,\"qc\":2}]",
		           UT50AddressEnumsMock::matcherSet3),
		ENUMS_SET4("enums set 4",
		           "[{\"source\":\"enums set 4\",\"fias_level\":\"4\",\"fias_actuality_state\":\"51\",\"capital_marker\":\"4\",\"beltway_hit\":\"OUT_KAD\",\"qc_geo\":3,\"qc_complete\":3,\"qc_house\":10,\"qc\":3}]",
		           UT50AddressEnumsMock::matcherSet4),
		ENUMS_SET5("enums set 5",
		           "[{\"source\":\"enums set 5\",\"fias_level\":\"5\",\"fias_actuality_state\":\"99\",\"capital_marker\":\"0\",\"beltway_hit\":\"\",\"qc_geo\":4,\"qc_complete\":4}]",
		           UT50AddressEnumsMock::matcherSet5),
		ENUMS_SET6("enums set 6",
		           "[{\"source\":\"enums set 6\",\"fias_level\":\"6\",\"qc_geo\":5,\"qc_complete\":5}]",
		           UT50AddressEnumsMock::matcherSet6),
		ENUMS_SET7("enums set 7",
		           "[{\"source\":\"enums set 7\",\"fias_level\":\"7\",\"qc_complete\":6}]",
		           UT50AddressEnumsMock::matcherSet7),
		ENUMS_SET8("enums set 8",
		           "[{\"source\":\"enums set 8\",\"fias_level\":\"8\",\"qc_complete\":7}]",
		           UT50AddressEnumsMock::matcherSet8),
		ENUMS_SET9("enums set 9",
		           "[{\"source\":\"enums set 9\",\"fias_level\":\"65\",\"qc_complete\":8}]",
		           UT50AddressEnumsMock::matcherSet9),
		ENUMS_SET10("enums set 10",
		            "[{\"source\":\"enums set 10\",\"fias_level\":\"90\",\"qc_complete\":9}]",
		            UT50AddressEnumsMock::matcherSet10),
		ENUMS_SET11("enums set 11",
		            "[{\"source\":\"enums set 11\",\"fias_level\":\"91\",\"qc_complete\":10}]",
		            UT50AddressEnumsMock::matcherSet11),
		ENUMS_SET12("enums set 12",
		            "[{\"source\":\"enums set 12\",\"fias_level\":\"-1\"}]",
		            UT50AddressEnumsMock::matcherSet12),
		ENUMS_UNKNOWN("enums unknown",
		              "[{\"source\":\"enums unknown\",\"fias_level\":\"999\",\"fias_actuality_state\":\"999\",\"capital_marker\":\"999\",\"beltway_hit\":\"OTHER\",\"qc_geo\":999,\"qc_complete\":999,\"qc_house\":999,\"qc\":999}]",
		              UT50AddressEnumsMock::matcherUnknown);

		private final String sourceAddress;
		private final String responseBody;
		private final Function<String, Matcher<Address>> matcher;

		SampleAddress(final String sourceAddress, final String responseBody, final Function<String, Matcher<Address>> matcher) {
			this.sourceAddress = sourceAddress;
			this.responseBody = responseBody;
			this.matcher = matcher;
		}

		public String getSourceAddress() {
			return sourceAddress;
		}

		public String getResponseBody() {
			return responseBody;
		}

		public Matcher<Address> getMatcher() {
			return matcher != null ? matcher.apply(sourceAddress) : nullValue(Address.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// enum fields omitted
	private static Matcher<Address> matcherOmitted(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(nullValue(FiasLevel.class)))
				.withProperty("fiasActualityState", is(nullValue(FiasActuality.class)))
				.withProperty("capitalMarker", is(nullValue(CapitalMarker.class)))
				.withProperty("beltwayHit", is(nullValue(BeltwayHit.class)))
				.withProperty("qcGeo", is(nullValue(QcGeo.class)))
				.withProperty("qcComplete", is(nullValue(QcAddressComplete.class)))
				.withProperty("qcHouse", is(nullValue(QcHouse.class)))
				.withProperty("qc", is(nullValue(QcAddress.class)));
	}

	// enum fields null
	private static Matcher<Address> matcherNull(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(nullValue(FiasLevel.class)))
				.withProperty("fiasActualityState", is(nullValue(FiasActuality.class)))
				.withProperty("capitalMarker", is(nullValue(CapitalMarker.class)))
				.withProperty("beltwayHit", is(nullValue(BeltwayHit.class)))
				.withProperty("qcGeo", is(nullValue(QcGeo.class)))
				.withProperty("qcComplete", is(nullValue(QcAddressComplete.class)))
				.withProperty("qcHouse", is(nullValue(QcHouse.class)))
				.withProperty("qc", is(nullValue(QcAddress.class)));
	}

	// enum fields empty
	private static Matcher<Address> matcherEmpty(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(nullValue(FiasLevel.class)))
				.withProperty("fiasActualityState", is(nullValue(FiasActuality.class)))
				.withProperty("capitalMarker", is(nullValue(CapitalMarker.class)))
				.withProperty("beltwayHit", is(BeltwayHit.NONE))
				.withProperty("qcGeo", is(nullValue(QcGeo.class)))
				.withProperty("qcComplete", is(nullValue(QcAddressComplete.class)))
				.withProperty("qcHouse", is(nullValue(QcHouse.class)))
				.withProperty("qc", is(nullValue(QcAddress.class)));
	}


	// enum unknown values
	private static Matcher<Address> matcherUnknown(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.UNKNOWN))
				.withProperty("fiasActualityState", is(FiasActuality.UNKNOWN))
				.withProperty("capitalMarker", is(CapitalMarker.UNKNOWN))
				.withProperty("beltwayHit", is(BeltwayHit.UNKNOWN))
				.withProperty("qcGeo", is(QcGeo.UNKNOWN))
				.withProperty("qcComplete", is(QcAddressComplete.UNKNOWN))
				.withProperty("qcHouse", is(QcHouse.UNKNOWN))
				.withProperty("qc", is(QcAddress.UNKNOWN));
	}

	// enum set 1
	private static Matcher<Address> matcherSet1(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.COUNTRY))
				.withProperty("fiasActualityState", is(FiasActuality.ACTUAL))
				.withProperty("capitalMarker", is(CapitalMarker.AREA_CENTER))
				.withProperty("beltwayHit", is(BeltwayHit.IN_MKAD))
				.withProperty("qcGeo", is(QcGeo.EXACT))
				.withProperty("qcComplete", is(QcAddressComplete.ADDRESS_COMPLETE))
				.withProperty("qcHouse", is(QcHouse.EXACT))
				.withProperty("qc", is(QcAddress.FULL));
	}

	// enum set 2
	private static Matcher<Address> matcherSet2(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.REGION))
				.withProperty("fiasActualityState", is(FiasActuality.RENAMED))
				.withProperty("capitalMarker", is(CapitalMarker.REGION_CENTER))
				.withProperty("beltwayHit", is(BeltwayHit.OUT_MKAD))
				.withProperty("qcGeo", is(QcGeo.NEAREST_HOUSE))
				.withProperty("qcComplete", is(QcAddressComplete.NO_REGION))
				.withProperty("qcHouse", is(QcHouse.SIMILAR))
				.withProperty("qc", is(QcAddress.PARTIAL));
	}

	// enum set 3
	private static Matcher<Address> matcherSet3(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.AREA))
				.withProperty("fiasActualityState", is(FiasActuality.RENAMED))
				.withProperty("capitalMarker", is(CapitalMarker.AREA_REGION_CENTER))
				.withProperty("beltwayHit", is(BeltwayHit.IN_KAD))
				.withProperty("qcGeo", is(QcGeo.STREET))
				.withProperty("qcComplete", is(QcAddressComplete.NO_CITY))
				.withProperty("qcHouse", is(QcHouse.RANGE))
				.withProperty("qc", is(QcAddress.UNRECOGNIZED));
	}

	// enum set 4
	private static Matcher<Address> matcherSet4(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.CITY))
				.withProperty("fiasActualityState", is(FiasActuality.RESUBORDINATED))
				.withProperty("capitalMarker", is(CapitalMarker.REGION_CENTRAL_AREA))
				.withProperty("beltwayHit", is(BeltwayHit.OUT_KAD))
				.withProperty("qcGeo", is(QcGeo.SETTLEMENT))
				.withProperty("qcComplete", is(QcAddressComplete.NO_STREET))
				.withProperty("qcHouse", is(QcHouse.NOT_FOUND))
				.withProperty("qc", is(QcAddress.INVARIANTS));
	}

	// enum set 5
	private static Matcher<Address> matcherSet5(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.CITYAREA))
				.withProperty("fiasActualityState", is(FiasActuality.REMOVED))
				.withProperty("capitalMarker", is(CapitalMarker.OTHER))
				.withProperty("beltwayHit", is(BeltwayHit.NONE))
				.withProperty("qcGeo", is(QcGeo.CITY))
				.withProperty("qcComplete", is(QcAddressComplete.NO_HOUSE));
	}

	// enum set 6
	private static Matcher<Address> matcherSet6(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.SETTLEMENT))
				.withProperty("qcGeo", is(QcGeo.UNDEFINED))
				.withProperty("qcComplete", is(QcAddressComplete.NO_FLAT));
	}

	// enum set 7
	private static Matcher<Address> matcherSet7(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.STREET))
				.withProperty("qcComplete", is(QcAddressComplete.ADDRESS_INCOMPLETE));
	}

	// enum set 8
	private static Matcher<Address> matcherSet8(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.HOUSE))
				.withProperty("qcComplete", is(QcAddressComplete.FOREIGN_ADDRESS));
	}

	// enum set 9
	private static Matcher<Address> matcherSet9(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.ESTATE))
				.withProperty("qcComplete", is(QcAddressComplete.POSTAL_BOX));
	}

	// enum set 10
	private static Matcher<Address> matcherSet10(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.AUX_TERRITORY))
				.withProperty("qcComplete", is(QcAddressComplete.VERIFY_PARSING));
	}

	// enum set 11
	private static Matcher<Address> matcherSet11(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.AUX_TERRITORY_STREET))
				.withProperty("qcComplete", is(QcAddressComplete.HOUSE_NOT_IN_FIAS));
	}

	// enum set 12
	private static Matcher<Address> matcherSet12(final String s) {
		return pojo(Address.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("fiasLevel", is(FiasLevel.FOREIGN_OR_EMPTY));
	}

	// =================================================================================================================

	@Test
	public void enumsOmitted() throws DaDataException {
		test(SampleAddress.ENUMS_OMITTED);
	}

	@Test
	public void enumsNull() throws DaDataException {
		test(SampleAddress.ENUMS_NULL);
	}

	@Test
	public void enumsEmpty() throws DaDataException {
		test(SampleAddress.ENUMS_EMPTY);
	}

	@Test
	public void enumsUnknown() throws DaDataException {
		test(SampleAddress.ENUMS_UNKNOWN);
	}

	@Test
	public void set1() throws DaDataException {
		test(SampleAddress.ENUMS_SET1);
	}

	@Test
	public void set2() throws DaDataException {
		test(SampleAddress.ENUMS_SET2);
	}

	@Test
	public void set3() throws DaDataException {
		test(SampleAddress.ENUMS_SET3);
	}

	@Test
	public void set4() throws DaDataException {
		test(SampleAddress.ENUMS_SET4);
	}

	@Test
	public void set5() throws DaDataException {
		test(SampleAddress.ENUMS_SET5);
	}

	@Test
	public void set6() throws DaDataException {
		test(SampleAddress.ENUMS_SET6);
	}

	@Test
	public void set7() throws DaDataException {
		test(SampleAddress.ENUMS_SET7);
	}

	@Test
	public void set8() throws DaDataException {
		test(SampleAddress.ENUMS_SET8);
	}

	@Test
	public void set9() throws DaDataException {
		test(SampleAddress.ENUMS_SET9);
	}

	@Test
	public void set10() throws DaDataException {
		test(SampleAddress.ENUMS_SET10);
	}

	@Test
	public void set11() throws DaDataException {
		test(SampleAddress.ENUMS_SET11);
	}

	@Test
	public void set12() throws DaDataException {
		test(SampleAddress.ENUMS_SET12);
	}

	// shared test body
	private void test(final SampleAddress address) {
		setupTestServer(server, URI, METHOD, address.getResponseBody());
		successTest(dadata, address.getSourceAddress(), address.getMatcher());
		server.verify();
	}
}

/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.unitTests;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.redcom.software.util.integration.api.client.dadata.dto.CompositeRequest;

import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import static ru.redcom.software.util.integration.api.client.dadata.dto.CompositeElementType.*;

public class UT10CompositeRequest {

	@Test
	public void arrayStyle() {
		final CompositeRequest.Record[] records = {
				new CompositeRequest.Record()
						.asIs("as is 1")
						.name("name 1")
						.address("address 1"),
				new CompositeRequest.Record()
						.asIs("asis 2a", "asis 2b")
						.name("name 2a", "name 2b")
						.address("address 2a", "address 2b"),
				new CompositeRequest.Record()
						.asIs("asis 3")
						.name("name 2a", "name 2b"),
				new CompositeRequest.Record()
						.asIs("as-is 4a", "as-is 4b", "as-is 4c"),
//				new CompositeRequest.Record()
//						.phone("7999999999"),   // should throw IllegalStateException: Element type is not in request structure
//				new CompositeRequest.Record()
//						.passport("0000 000000"),   // should throw IllegalStateException: Element type is not in request structure
				new CompositeRequest.Record()  // should do nothing
		};

		final CompositeRequest request = CompositeRequest
				.compose(AS_IS, NAME, ADDRESS)
				.records(records)
				.build();
		testCompositeRequest(request, "CompositeRequest(structure=[AS_IS, NAME, ADDRESS], data=[CompositeRequest.Record(recordElements={AS_IS=as is 1, NAME=name 1, ADDRESS=address 1}), CompositeRequest.Record(recordElements={AS_IS=[asis 2a, asis 2b], NAME=[name 2a, name 2b], ADDRESS=[address 2a, address 2b]}), CompositeRequest.Record(recordElements={AS_IS=asis 3, NAME=[name 2a, name 2b], ADDRESS=null}), CompositeRequest.Record(recordElements={AS_IS=[as-is 4a, as-is 4b, as-is 4c], NAME=null, ADDRESS=null})])");
	}

	@Test
	public void arrayStyleUnconstrained() {
		final CompositeRequest.Record[] records = {
				new CompositeRequest.Record()
						.asIs("as is 1")
						.name("name 1")
						.address("address 1"),
				new CompositeRequest.Record()
						.asIs("asis 2a", "asis 2b")
						.name("name 2a", "name 2b")
						.address("address 2a", "address 2b"),
				new CompositeRequest.Record()
						.asIs("asis 3")
						.name("name 2a", "name 2b"),
				new CompositeRequest.Record()
						.asIs("as-is 4a", "as-is 4b", "as-is 4c")
		};

		final CompositeRequest request = CompositeRequest
				.compose()
				.records(records)
				.build();
		testCompositeRequest(request, "CompositeRequest(structure=[AS_IS, NAME, ADDRESS], data=[CompositeRequest.Record(recordElements={AS_IS=as is 1, NAME=name 1, ADDRESS=address 1}), CompositeRequest.Record(recordElements={AS_IS=[asis 2a, asis 2b], NAME=[name 2a, name 2b], ADDRESS=[address 2a, address 2b]}), CompositeRequest.Record(recordElements={AS_IS=asis 3, NAME=[name 2a, name 2b], ADDRESS=null}), CompositeRequest.Record(recordElements={AS_IS=[as-is 4a, as-is 4b, as-is 4c], NAME=null, ADDRESS=null})])");
	}

	@Test
	public void builderStyle() {
		// @formatter:off
		final CompositeRequest request = CompositeRequest
				.compose(AS_IS, NAME, ADDRESS)
					.record()
						.asIs("as is 1")
						.name("name 1")
						.address("address 1")
				.and()
					.record()
						.asIs("asis 2a", "asis 2b")
						.name("name 2a", "name 2b")
						.address("address 2a", "address 2b")
				.and()
					.record()
						.asIs("asis 3")
						.name("name 2a", "name 2b")
				.and()
					.record()
						.asIs("as-is 4a", "as-is 4b", "as-is 4c")
//				.and()
//					.record()
//						.phone("7999999999")    // should throw IllegalStateException: Element type is not in request structure
//				.and()
//					.record()  // should throw IllegalStateException: Element is empty
				.and()
					.build();
		// @formatter:on
		testCompositeRequest(request, "CompositeRequest(structure=[AS_IS, NAME, ADDRESS], data=[CompositeRequest.Record(recordElements={AS_IS=as is 1, NAME=name 1, ADDRESS=address 1}), CompositeRequest.Record(recordElements={AS_IS=[asis 2a, asis 2b], NAME=[name 2a, name 2b], ADDRESS=[address 2a, address 2b]}), CompositeRequest.Record(recordElements={AS_IS=asis 3, NAME=[name 2a, name 2b], ADDRESS=null}), CompositeRequest.Record(recordElements={AS_IS=[as-is 4a, as-is 4b, as-is 4c], NAME=null, ADDRESS=null})])");
	}

	@Test
	public void builderStyleUnconstrained() {
		// @formatter:off
		final CompositeRequest request = CompositeRequest
				.compose()
					.record()
						.asIs("as is 1")
						.name("name 1")
						.address("address 1")
				.and()
					.record()
						.asIs("asis 2a", "asis 2b")
						.name("name 2a", "name 2b")
						.address("address 2a", "address 2b")
				.and()
					.record()
						.asIs("asis 3")
						.name("name 2a", "name 2b")
				.and()
					.record()
						.asIs("as-is 4a", "as-is 4b", "as-is 4c")
				.and()
					.build();
		// @formatter:on
		testCompositeRequest(request, "CompositeRequest(structure=[AS_IS, NAME, ADDRESS], data=[CompositeRequest.Record(recordElements={AS_IS=as is 1, NAME=name 1, ADDRESS=address 1}), CompositeRequest.Record(recordElements={AS_IS=[asis 2a, asis 2b], NAME=[name 2a, name 2b], ADDRESS=[address 2a, address 2b]}), CompositeRequest.Record(recordElements={AS_IS=asis 3, NAME=[name 2a, name 2b], ADDRESS=null}), CompositeRequest.Record(recordElements={AS_IS=[as-is 4a, as-is 4b, as-is 4c], NAME=null, ADDRESS=null})])");
	}

	@Test
	public void builderStyleEverything() {
		// @formatter:off
		final CompositeRequest request = CompositeRequest
				.compose(AS_IS, ADDRESS, BIRTHDATE, EMAIL, NAME, PASSPORT, PHONE, VEHICLE)
					.record()
						.asIs("as is 1")
						.address("address 1")
						.birthDate("birthdate 1")
						.email("email 1")
						.name("name 1")
						.passport("passport 1")
						.phone("phone 1")
						.vehicle("vehicle 1")
				.and()
					.record()
						.asIs("asis 2a", "asis 2b")
						.address("address 2a", "address 2b")
						.birthDate("birthdate 2a", "birthdate 2b")
						.email("email 2a", "email 2b")
						.name("name 2a", "name 2b")
						.passport("passport 2a", "passport 2b")
						.phone("phone 2a", "phone 2b")
						.vehicle("vehicle 2a", "vehicle 2b")
				.and()
					.record()
						.asIs("asis 3")
						.name("name 2a", "name 2b")
				.and()
					.record()
						.asIs("as-is 4a", "as-is 4b", "as-is 4c")
//				.and()
//					.record()  // should throw IllegalStateException: Element is empty
				.and()
					.build();
		// @formatter:on
		testCompositeRequest(request, "CompositeRequest(structure=[AS_IS, NAME, ADDRESS, BIRTHDATE, PASSPORT, PHONE, EMAIL, VEHICLE], data=[CompositeRequest.Record(recordElements={AS_IS=as is 1, NAME=name 1, ADDRESS=address 1, BIRTHDATE=birthdate 1, PASSPORT=passport 1, PHONE=phone 1, EMAIL=email 1, VEHICLE=vehicle 1}), CompositeRequest.Record(recordElements={AS_IS=[asis 2a, asis 2b], NAME=[name 2a, name 2b], ADDRESS=[address 2a, address 2b], BIRTHDATE=[birthdate 2a, birthdate 2b], PASSPORT=[passport 2a, passport 2b], PHONE=[phone 2a, phone 2b], EMAIL=[email 2a, email 2b], VEHICLE=[vehicle 2a, vehicle 2b]}), CompositeRequest.Record(recordElements={AS_IS=asis 3, NAME=[name 2a, name 2b], ADDRESS=null, BIRTHDATE=null, PASSPORT=null, PHONE=null, EMAIL=null, VEHICLE=null}), CompositeRequest.Record(recordElements={AS_IS=[as-is 4a, as-is 4b, as-is 4c], NAME=null, ADDRESS=null, BIRTHDATE=null, PASSPORT=null, PHONE=null, EMAIL=null, VEHICLE=null})])");
	}

	@Test
	public void builderStyleEverythingUnconstained() {
		// @formatter:off
		final CompositeRequest request = CompositeRequest
				.compose()
					.record()
						.asIs("as is 1")
						.address("address 1")
						.birthDate("birthdate 1")
						.email("email 1")
						.name("name 1")
						.passport("passport 1")
						.phone("phone 1")
						.vehicle("vehicle 1")
				.and()
					.record()
						.asIs("asis 2a", "asis 2b")
						.address("address 2a", "address 2b")
						.birthDate("birthdate 2a", "birthdate 2b")
						.email("email 2a", "email 2b")
						.name("name 2a", "name 2b")
						.passport("passport 2a", "passport 2b")
						.phone("phone 2a", "phone 2b")
						.vehicle("vehicle 2a", "vehicle 2b")
				.and()
					.record()
						.asIs("asis 3")
						.name("name 2a", "name 2b")
				.and()
					.record()
						.asIs("as-is 4a", "as-is 4b", "as-is 4c")
				.and()
					.build();
		// @formatter:on
		testCompositeRequest(request, "CompositeRequest(structure=[AS_IS, NAME, ADDRESS, BIRTHDATE, PASSPORT, PHONE, EMAIL, VEHICLE], data=[CompositeRequest.Record(recordElements={AS_IS=as is 1, NAME=name 1, ADDRESS=address 1, BIRTHDATE=birthdate 1, PASSPORT=passport 1, PHONE=phone 1, EMAIL=email 1, VEHICLE=vehicle 1}), CompositeRequest.Record(recordElements={AS_IS=[asis 2a, asis 2b], NAME=[name 2a, name 2b], ADDRESS=[address 2a, address 2b], BIRTHDATE=[birthdate 2a, birthdate 2b], PASSPORT=[passport 2a, passport 2b], PHONE=[phone 2a, phone 2b], EMAIL=[email 2a, email 2b], VEHICLE=[vehicle 2a, vehicle 2b]}), CompositeRequest.Record(recordElements={AS_IS=asis 3, NAME=[name 2a, name 2b], ADDRESS=null, BIRTHDATE=null, PASSPORT=null, PHONE=null, EMAIL=null, VEHICLE=null}), CompositeRequest.Record(recordElements={AS_IS=[as-is 4a, as-is 4b, as-is 4c], NAME=null, ADDRESS=null, BIRTHDATE=null, PASSPORT=null, PHONE=null, EMAIL=null, VEHICLE=null})])");
	}


	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void emptyElement() {
		exception.expect(IllegalStateException.class);
		exception.expectMessage("Record contains no elements");
		// @formatter:off
		CompositeRequest
				.compose(AS_IS)
					.record()
				.and()
					.build();
		// @formatter:on
	}

	@Test
	public void elementNotInStructure() {
		exception.expect(IllegalStateException.class);
		exception.expectMessage("Elements not in the structure: {PHONE=7999999999}");
		// @formatter:off
		CompositeRequest
				.compose(AS_IS)
					.record()
						.phone("7999999999")    // should throw IllegalStateException: Element type is not in request structure
				.and()
					.build();
		// @formatter:on
	}

	@Test
	public void elementsNotInStructure() {
		exception.expect(IllegalStateException.class);
		exception.expectMessage("Elements not in the structure: {AS_IS=as is 2, NAME=name 2, ADDRESS=address 2}, {PHONE=7999999999}");
		final CompositeRequest.Record[] records = {
				new CompositeRequest.Record()
						.asIs("as is 1"),
				new CompositeRequest.Record()
						.asIs("as is 2")
						.name("name 2")
						.address("address 2"),
				new CompositeRequest.Record()
						.phone("7999999999"),   // should throw IllegalStateException: Element type is not in request structure
				new CompositeRequest.Record()  // should do nothing
		};
		CompositeRequest
				.compose(AS_IS)
				.records(records)
				.build();
	}

	@Test
	public void elementAnd() {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage("This method should be used only with builder-style composition");
		new CompositeRequest.Record().and();
	}

	private void testCompositeRequest(final CompositeRequest request, final String pattern) {
		System.out.println("request:");
		System.out.println(request);
		assertThat(request, hasToString(pattern));
	}
}

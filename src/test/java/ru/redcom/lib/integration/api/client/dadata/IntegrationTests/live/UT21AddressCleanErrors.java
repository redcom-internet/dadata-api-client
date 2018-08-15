/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests.live;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientException;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;
import ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesError.SampleErrorAddresses;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@IfProfileValue(name = "live-tests", value = "enabled")
public class UT21AddressCleanErrors {

	@Autowired
	private DaDataClient dadata;

	@Test(expected = IllegalArgumentException.class)
	public void nullSingleAddress() throws DaDataException {
		//noinspection ConstantConditions
		dadata.cleanAddress(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptySingleAddress() throws DaDataException {
		dadata.cleanAddress("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullAddressArray() throws DaDataException {
		//noinspection ConstantConditions
		dadata.cleanAddresses((String[]) null);
	}

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void emptyAddressArray() throws DaDataException {
		test(SampleErrorAddresses.EMPTY_ARRAY);
	}

	@Test
	public void nullAddressArrayElement() throws DaDataException {
		test(SampleErrorAddresses.NULL_ARRAY_ELEMENT);
	}

	@Test
	public void emptyAddressArrayElement() throws DaDataException {
		test(SampleErrorAddresses.EMPTY_ARRAY_ELEMENT);
	}

	// CREDENTIALS MISSING
	@Test
	public void credentialsBadFormat() throws DaDataException {
		final DaDataClient dadata = DaDataClientFactory.getInstance("testInvalidApiKey", "testInvalidApiSecret");
		test(dadata, SampleErrorAddresses.CREDENTIALS_BAD_FORMAT);
	}

	@Test
	public void credentialsMissing() throws DaDataException {
		final DaDataClient dadata = DaDataClientFactory.getInstance("0000000000000000000000000000000000000000", "0000000000000000000000000000000000000000");
		test(dadata, SampleErrorAddresses.CREDENTIALS_MISSING);
	}

	// CREDENTIALS INVALID
	@Test
	public void credentialsInvalid() throws DaDataException {
		final DaDataClient dadata = DaDataClientFactory.getInstance(CommonLive.API_KEY, "0000000000000000000000000000000000000000");
		test(dadata, SampleErrorAddresses.CREDENTIALS_INVALID);
	}

/*
	// BALANCE EXHAUSED
	// TODO проходит, но сложно создать условия, разве что сделать для теста специальный профиль с вечно пустым балансом
	@Test
	public void balanceExhausted() throws DaDataException {
		test(dadata, SampleErrorAddresses.BALANCE_EXHAUSTED);
	}
*/

	// -UNSUPPORTED METHOD - API не предусматривает посылку запроса с неправильным типом

/*
	// TOO_MANY_ITEMS
	@Test
	public void tooManyItems() throws DaDataException {
		// TODO почему-то выдаёт не тот код ответа - 400 вместо 413
		test(dadata, SampleErrorAddresses.TOO_MANY_ITEMS);
		//test(dadata, SampleErrorAddresses.TOO_MANY_ITEMS1);
	}
*/

/*
	// TOO_MANY_REQUESTS
	@Test
	public void tooManyRequests() throws DaDataException {
		final SampleErrorAddresses sample = SampleErrorAddresses.TOO_MANY_REQUESTS;
		exception.expect(DaDataClientException.class);
		exception.expectMessage(sample.getMessage());
		exception.expect(sample.getMatcher());
		// TODO никак не удаётся получить эту ошибку.
		// ни при последовательном выполнении
		for (int i = 0; i < 100; i ++)
			dadata.cleanAddresses(sample.getArgument());
		// ни при параллельном
		final int parallelism = 10;
		final ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);
		try(Closeable ignored = forkJoinPool::shutdown) {
			forkJoinPool.submit(
					() -> IntStream.rangeClosed(0, 99)
					               .parallel()
					               .forEach((i) -> dadata.cleanAddresses(sample.getArgument()))
			                   ).get();
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
*/

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleErrorAddresses sample) {
		test(this.dadata, sample);
	}

	private void test(final DaDataClient dadata, final SampleErrorAddresses sample) {
		exception.expect(DaDataClientException.class);
		exception.expectMessage(sample.getMessage());
		exception.expect(sample.getMatcher());
		dadata.cleanAddresses(sample.getArgument());
/*
		try {
			dadata.cleanAddresses((String[]) sample.getArgument());
		} catch (DaDataClientException e) {
			System.out.println("DaData client exception: " + e);
			System.out.println("HTTP status: " + e.getHttpStatusCode());
			System.out.println("HTTP status text: " + e.getHttpStatusText());
			System.out.println("API error code: " + e.getApiErrorCode());
			System.out.println("API error message: " + e.getApiErrorMessage());
			if (e.getApiErrorMessage() != null) {
				System.out.println("API error data: " + Arrays.toString(e.getApiErrorMessage().getData()));
				System.out.println("API error details: " + Arrays.toString(e.getApiErrorMessage().getDetails()));
				System.out.println("API error detail: " + e.getApiErrorMessage().getDetail());
				System.out.println("API error contents: " + e.getApiErrorMessage().getContents());
			}
			System.out.println("Suppressed exceptions: " + Arrays.toString(e.getSuppressed()));
		}
*/
	}

}

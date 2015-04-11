package us.hexcoder.java8.betamax;

import co.freeside.betamax.ProxyConfiguration;
import co.freeside.betamax.TapeMode;
import co.freeside.betamax.junit.Betamax;
import co.freeside.betamax.junit.RecorderRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import us.hexcoder.java8.betamax.configuration.ApplicationConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 67726e on 4/10/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfiguration.class})
@TestExecutionListeners({
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class
})
public class ExampleTest {
	@Rule
	public RecorderRule recorderRule = new RecorderRule(ProxyConfiguration.builder()
			.tapeRoot(new File("src\\test\\resources\\betamax"))
			.sslEnabled(true)
			.build());

	@Test(expected = HttpClientErrorException.class)
	@Betamax(tape = "twitter_search", mode = TapeMode.READ_WRITE)
	public void testSomething() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("q", "betamax");

		new RestTemplate().getForObject("https://api.twitter.com/1.1/search/tweets.json", String.class, parameters);
	}
}

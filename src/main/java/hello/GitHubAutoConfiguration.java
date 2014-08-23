/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hello;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubConnectionFactory;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Social connectivity with
 * Twitter.
 * 
 * @author Craig Walls
 * @author Kris De Volder
 * @since ???
 */
@Configuration
@ConditionalOnClass({ SocialConfigurerAdapter.class, GitHubConnectionFactory.class })
@ConditionalOnProperty(prefix = "spring.social.github.", value = "appId")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class GitHubAutoConfiguration {

	@Configuration
	@EnableSocial
	@ConditionalOnWebApplication
	protected static class GitAutoConfigurationAdapter extends
			SocialAutoConfigurerAdapter {

		@Override
		protected String getPropertyPrefix() {
			return "spring.social.github.";
		}

		@Override
		protected ConnectionFactory<?> createConnectionFactory(RelaxedPropertyResolver properties) {
			return new GitHubConnectionFactory(properties.getRequiredProperty("appId"),
					properties.getRequiredProperty("appSecret"));
		}

		@Bean
		@ConditionalOnMissingBean
		@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
		public GitHub github(ConnectionRepository repository) {
			Connection<GitHub> connection = repository
					.findPrimaryConnection(GitHub.class);
			if (connection != null) {
				return connection.getApi();
			}
// The twitter one has something like this: do we need that for github? It seems to work fine without this
//			String id = getProperties().getRequiredProperty("app-id");
//			String secret = getProperties().getRequiredProperty("app-secret");
//			return new GitHubTemplate(id, secret);
			throw new RuntimeException("No github connection.");
		}

// The example doesn't seem to need this so commented out. 		
//		@Bean(name = { "connect/githubConnect", "connect/githubConnected" })
//		@ConditionalOnProperty(prefix = "spring.social.", value = "auto-connection-views")
//		public View githubConnectView() {
//			return new GenericConnectionStatusView("github", "GitHub");
//		}

	}

}

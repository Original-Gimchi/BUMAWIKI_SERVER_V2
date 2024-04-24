package com.project.bumawiki.global.service;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.*;

import java.util.Collections;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.arbitraries.StringArbitrary;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.thumbsup.domain.ThumbsUp;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

public class FixtureGenerator {

	private static final FixtureMonkey fixtureGenerator = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	public static ArbitraryBuilder<Docs> getDefaultDocsBuilder() {
		return fixtureGenerator.giveMeBuilder(Docs.class)
			.setNull(javaGetter(Docs::getId))
			.set(javaGetter(Docs::getVersionDocs), Collections.emptyList());
	}

	public static ArbitraryBuilder<VersionDocs> getDefaultVersionDocsBuilder(Docs docs, User user) {
		return fixtureGenerator.giveMeBuilder(VersionDocs.class)
			.set(javaGetter(VersionDocs::getUser), user)
			.set(javaGetter(VersionDocs::getDocs), docs);
	}

	public static ArbitraryBuilder<User> getDefaultUserBuilder() {
		return fixtureGenerator.giveMeBuilder(User.class)
			.setNull(javaGetter(User::getId))
			.set(javaGetter(User::getAuthority), Authority.USER);
	}

	public static ArbitraryBuilder<ThumbsUp> getDefaultThumbsUpBuilder(Docs docs, User user) {
		return fixtureGenerator.giveMeBuilder(ThumbsUp.class)
			.setNull(javaGetter(ThumbsUp::getId))
			.set(javaGetter(ThumbsUp::getUser), user)
			.set(javaGetter(ThumbsUp::getDocs), docs);
	}

	public static StringArbitrary getDefaultStringBuilder() {
		return Arbitraries.strings()
			.ascii()
			.ofMinLength(1);
	}
}

package com.project.bumawiki.domain.contribute.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.contribute.domain.Contribute;
import com.project.bumawiki.domain.contribute.domain.repository.ContributeRepository;
import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.exception.DocsNotFoundException;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.domain.user.exception.UserNotFoundException;
import com.project.bumawiki.global.annotation.ServiceWithTransactionalReadOnly;
import com.project.bumawiki.global.util.SecurityUtil;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@ServiceWithTransactionalReadOnly
@RequiredArgsConstructor
public class ContributeService {

	private final DocsRepository docsRepository;
	private final UserRepository userRepository;
	private final ContributeRepository contributeRepository;

	@Transactional
	public Contribute setContribute(VersionDocs versionDocs) {

		User user = findUser();

		Docs docs = docsRepository.findById(versionDocs.getDocsId())
			.orElseThrow(() -> DocsNotFoundException.EXCEPTION);

		return setContributeAtUserAndDocs(versionDocs, user, docs);
	}

	@NotNull
	private Contribute setContributeAtUserAndDocs(VersionDocs versionDocs, User user, Docs docs) {
		Contribute contribute = createContribute(docs, user, versionDocs);

		ArrayList<Contribute> contributes = new ArrayList<>();
		contributes.add(contribute);

		setFirstContribute(contributes, user);
		docs.updateContributor(contributes);
		return contribute;
	}

	@Transactional
	public Contribute updateContribute(final VersionDocs versionDocs) {

		User user = findUser();

		Docs docs = docsRepository.findById(versionDocs.getDocsId())
			.orElseThrow(() -> DocsNotFoundException.EXCEPTION);

		Contribute contribute = createContribute(docs, user, versionDocs);

		if (user.getContributeDocs() == null) {
			ArrayList<Contribute> contributes = new ArrayList<>();
			contributes.add(contribute);

			setFirstContribute(contributes, user);
		}
		docs.getContributor().add(contribute);

		return contribute;
	}

	@Transactional
	public void setFirstContribute(final List<Contribute> contributes, final User user) {
		user.updateContributeDocs(contributes);
	}

	@Transactional
	private Contribute createContribute(Docs docs, User user, VersionDocs versionDocs) {
		return contributeRepository.save(
			Contribute.builder()
				.docs(docs)
				.contributor(user)
				.versionDocs(versionDocs)
				.createdAt(LocalDateTime.now())
				.build()
		);
	}

	private User findUser() {
		User user = SecurityUtil.getCurrentUserWithLogin();

		return userRepository.findById(user.getId())
			.orElseThrow(() -> UserNotFoundException.EXCEPTION);
	}
}

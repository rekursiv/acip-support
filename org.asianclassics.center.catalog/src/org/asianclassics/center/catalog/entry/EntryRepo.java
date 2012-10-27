package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.link.LinkManager;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EntryRepo extends CouchDbRepositorySupport<EntryModel> {

	@Inject
	public EntryRepo(LinkManager lm) {
		super(EntryModel.class, lm.getDb("catalog"), false);
	}
		

}

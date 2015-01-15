function(doc) {
	if (doc.recordType==='Collection')
		emit( [doc.recordType, doc.name], null );
	else if (doc.recordType==='Source')
		emit([doc.recordType, doc.projectPriority, doc.collectionId, doc.volumeIndex, doc.pageIndex], null);
	else emit(doc.recordType, null)
}


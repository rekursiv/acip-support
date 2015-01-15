function(doc) {
	if (doc.type==='Collection')
		emit( [doc.type, doc.name], null );
	else if (doc.type==='Source')
		emit([doc.type, doc.projectPriority, doc.collectionId, doc.volumeIndex, doc.pageIndex], null);
	else emit(doc.type, null)
}


function(doc) {
	if (doc.recordType==='Collection')
		emit( [doc.recordType, doc.name], null );
	else if (doc.recordType==='Page')
		emit([doc.recordType, doc.projectPriority, doc.collectionId, doc.volumeIndex, doc.pageIndex], [doc.dispatchedTo, doc.text]);
	else if (doc.recordType==='InputTask')
		emit([doc.recordType, doc.worker, doc.taskPriority, doc.projectPriority, doc.collectionId, doc.bookIndex, doc.pageIndex], ["A:"+doc.isActive, "F:"+doc.isFinal, doc.product]);
	else emit(doc.recordType, null);
}


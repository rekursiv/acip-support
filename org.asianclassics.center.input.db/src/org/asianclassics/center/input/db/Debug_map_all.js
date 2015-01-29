function(doc) {
	if (doc.recordType==='Collection')
		emit( [doc.recordType, doc.name], null );
	else if (doc.recordType==='Page')
		emit([doc.recordType, doc.projectPriority, doc.collectionId, doc.volumeIndex, doc.pageIndex], 
				[doc.dispatchedTo, doc.text==null?null:doc.text.substring(0, 30)]);
	else if (doc.recordType==='InputTask')
		emit([doc.recordType, doc.worker, doc.taskIndex, doc.projectPriority, doc.collectionId, doc.bookIndex, doc.pageIndex], 
				["A:"+doc.isActive, "F:"+doc.isFinal, doc.product==null?null:doc.product.substring(0, 30)]);
	else emit(doc.recordType, null);
}


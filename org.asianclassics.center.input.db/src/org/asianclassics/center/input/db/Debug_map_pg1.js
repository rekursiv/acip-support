function(doc) {
	if (doc.recordType==='Page' && doc.pageIndex==1)
		emit([doc.recordType, doc.projectPriority, doc.collectionId, doc.volumeIndex, doc.pageIndex], 
				[doc.dispatchedTo, doc.text==null?null:doc.text.substring(0, 30)]);
	else if (doc.recordType==='InputTask' && doc.pageIndex==1)
		emit([doc.recordType, doc.worker, doc.taskPriority, doc.projectPriority, doc.collectionId, doc.bookIndex, doc.pageIndex], 
				["A:"+doc.isActive, "F:"+doc.isFinal, doc.product==null?null:doc.product.substring(0, 30)]);
}


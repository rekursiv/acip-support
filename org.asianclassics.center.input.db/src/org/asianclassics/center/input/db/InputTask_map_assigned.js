function(doc) {
	if (doc.recordType==='InputTask' && doc.isActive && doc.worker)
		emit([doc.worker, doc.taskIndex, doc.projectPriority, doc.collectionId, doc.bookIndex, doc.pageIndex], null)
}

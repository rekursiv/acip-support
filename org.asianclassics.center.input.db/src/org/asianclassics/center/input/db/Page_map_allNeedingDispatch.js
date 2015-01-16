function(doc) {
	if (doc.recordType === 'Page' && !doc.dispatch) 
		emit([doc.projectPriority, doc.collectionId, doc.bookIndex, doc.pageIndex], null)
}

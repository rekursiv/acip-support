function(doc) {
	if (doc.recordType === 'Page' && !doc.dispatchedTo) 
		emit([doc.projectPriority, doc.collectionId, doc.bookIndex, doc.pageIndex], null)
}

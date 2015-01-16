function(doc) {
	if (doc.recordType === 'Page')
		emit(doc.collectionId, null)
}

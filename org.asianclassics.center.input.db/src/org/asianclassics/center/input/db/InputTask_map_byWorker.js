function(doc) {
	if (doc.recordType==='InputTask')
		emit(doc.worker, null)
}

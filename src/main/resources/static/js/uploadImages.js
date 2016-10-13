Dropzone.options.imageUploadDropzone = {
    paramName: "file", // The name that will be used to transfer the file
    init: function() {
        this.on("success", function(file) {
            appendToFileList(file.name);
            appendToForm(file.name);
        });
    },
    maxFilesize: 10 // MB
};

document.addEventListener("DOMContentLoaded", function() {
	dragula([element("file-list")])
	.on('drop', function (el) {
		clearImageOrderData();
		var files = document.getElementsByClassName("draggable-file");
		for (var i = 0; i < files.length; i++) {
			var file = files[i];
			if (!file.classList.contains("gu-mirror")) {
				// Don't add the one that is floating/being dragged that would add two of the same.
				appendToForm(file.innerHTML);
			}
		}
	});
});

function clearImageOrderData() {
    element("image-order-data").innerHTML = "";
}

function appendToForm(filename) {
    element("image-order-data").innerHTML += "<input type='hidden' name='filename' value='" + filename + "'>";
}

function appendToFileList(name) {
    element("file-list").innerHTML += "<div class='draggable-file'>" + name + "</div>";
}

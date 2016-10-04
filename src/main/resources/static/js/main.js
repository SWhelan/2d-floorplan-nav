Dropzone.options.imageUploadDropzone = {
    paramName: "file", // The name that will be used to transfer the file
    init: function() {
        this.on("success", function(file) {
            appendToFileList(file.name);
        });
    },
    maxFilesize: 10 // MB
};

function appendToFileList(name) {
    document.getElementById("file-list").innerHTML += "<p>" + name + "</p>";
}
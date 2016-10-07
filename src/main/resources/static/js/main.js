Dropzone.options.imageUploadDropzone = {
    paramName: "file", // The name that will be used to transfer the file
    init: function() {
        this.on("success", function(file) {
            appendToFileList(file.name);
        });
    },
    maxFilesize: 10 // MB
};

document.addEventListener("DOMContentLoaded", function() {
   dragula([element("file-list")])
    .on('drop', function (el) {
       console.log("dropped");
    });
});

function appendToFileList(name) {
    element("file-list").innerHTML += "<div class='draggable-file'>" + name + "</div>";
}

function element(id) {
    return document.getElementById(id);
}
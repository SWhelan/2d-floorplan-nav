var pointA = null;
var pointB = null;
var excludePoints = [];
var currWindowWidth = null;
var currWindowHeight = null;
var prevWindowWidth = null;
var prevWindowHeight = null;
var END_POINT_COLOR = "#33C3F0"; // From skeleton.css to match theme.
var EXCLUDE_POINT_COLOR = "red";
var ANNOTATION_POINT_COLOR = "green";
var directions = null;
var directionNumber = 0;
var annotations = [];
var corners = [];
var fistCornerId = null;
var uniqueClickId = 0;

document.addEventListener("DOMContentLoaded", function() {
	initPage();
});

function initPage() {
	addClickHandlersForPicking();	
	window.onresize = handleResize;
	saveWindowSize();
	selectPickPointsButton();
}

function pickPointsMode() {
	removeClickHandlers();
	addClickHandlersForPicking();
	selectPickPointsButton();
	changeCursor("pointer");
}

function excludePointsMode() {
	removeClickHandlers();
	addClickHandlersForExcluding();
	selectExcludePointsButton();
	changeCursor("url('images/paintingcursor.ico'), pointer");
}

function submitData() {
	displayLoadingScreen();
	sendData();
}

function handleResponse(data) {
	hideLoadingScreen();
	var response = JSON.parse(data.currentTarget.response);
	var steps = response.prettySteps;
	displaySteps(steps);
	swapImages(response.afterPathFileNames);
}

function addClickHandlersForPicking() {
	setClickHandlersOnFloors(function(event) {
		addPoint(event);
	});
}

function addClickHandlersForExcluding() {
	setClickHandlersOnFloors(function(event) {
		excludePoint(event);		
	});
	var floors = getFloors();
	for (var i = 0; i < floors.length; i++) {
		floors[i].ondrag = function(event) {
			if(Math.random() > .3) {
				excludePoint(event);
			}
		};
	}
}

function removeClickHandlers() {
	setClickHandlersOnFloors(null);
}

function setClickHandlersOnFloors(onClickFunction) {
	var floors = getFloors();
	for (var i = 0; i < floors.length; i++) {
		floors[i].onclick = onClickFunction;
	}
}

function selectExcludePointsButton() {
	greyButton("exclude");
	whiteButton("pick");
	whiteButton("annotation");
}

function selectPickPointsButton() {
	greyButton("pick");
	whiteButton("exclude");
	whiteButton("annotation");
}

function selectAnnotationButton() {
	whiteButton("pick");
	whiteButton("exclude");
	greyButton("annotation");
}

function greyButton(value) {
	element(value + "-points-button").style.background = "lightgrey";
}

function whiteButton(value) {
	element(value + "-points-button").style.background = "white";
}

function addPoint(event) {
	var color = END_POINT_COLOR;
	if(pointB != null) {
		return;
	}
	
	var point = getClickInformation(event);
	if (pointA == null) {
		pointA = point;
		displayClick(event, color);
	} else if(pointB == null) {
		pointB = point;
		displayClick(event, color);
	}
}

function excludePoint(event) {
	var point = getClickInformation(event);
	if (point.xcoord > 0) {
		excludePoints.push(point);
		displayClick(event, EXCLUDE_POINT_COLOR);
	}
}

function resetPickPoints() {
	pointA = null;
	pointB = null;
	resetPoints(false);
}

function resetExcludePoints() {
	excludePoints = [];
	resetPoints(true);
}

function resetPoints(exclude) {
	var temp = document.getElementsByClassName("point");
	var points = [];
	for(var i = 0; i < temp.length; i++) {
		points[i] = temp[i];
	}
	for(var i = 0; i < points.length; i++) {
		var point = points[i];
		if ((exclude && point.dataset.color === EXCLUDE_POINT_COLOR) || (!exclude && point.dataset.color === END_POINT_COLOR)) {
			point.parentNode.removeChild(point);
		}
	}
}

function annotationMode() {
	selectAnnotationButton();
	document.body.style.cursor = "crosshair";
	removeClickHandlers();
	setClickHandlersOnFloors(function(event) {
		var dotId = displayClick(event, ANNOTATION_POINT_COLOR);
		var corner = {};
		corner.data = getClickInformation(event);
		corner.id = dotId;
		corners.push(corner);
		if(corners.length == 1) {
			firstCornerId = dotId;
		}
		if(corners.length > 0) {
			element(firstCornerId).onclick = function() {
				var title = prompt("Name the annotation:");
				var annotation = {};
				annotation.title = title;
				annotation.corners = corners;	
				annotations.push(annotation);
				finalizeAnnotation(annotation);
				corners = [];
				updateAnnotationsList();
			}
		}
	});
}

function finalizeAnnotation(annotation) {
	var corners = annotation.corners;
	var avgX = 0;
	var avgY = 0;
	var displayX = 0;
	var displayY = 0;
	for (var i = 0; i < corners.length; i++) {
		element(corners[i].id).style.width = "13px";
		element(corners[i].id).style.height = "13px";
		avgX = avgX + corners[i].data.xcoord;
		avgY = avgY + corners[i].data.ycoord;
		displayX = displayX + corners[i].data.displayWidth;
		displayY = displayY + corners[i].data.displayHeight;
	}
	
	avgX = avgX / corners.length;
	avgY = avgY / corners.length;
	displayX = displayX / corners.length;
	displayY = displayY / corners.length;
	
	displayClickFromPoint(display, displayY, "purple");
}

function updateAnnotationsList() {
	
}

function displayClick(event, color) {
	return displayClickFromPoint(event.pageX, event.pageY, color);
}

function displayClickFromPoint(x, y, color) {
	var holder = element("point-holder");
	var margin = 15;
	var top = (y - margin);
	var left = Math.round(x - holder.getBoundingClientRect().left - margin);
	uniqueClickId++;
	holder.innerHTML += "<div id='" + parseInt(uniqueClickId) +"' class='point' data-color='" + color + "' style='background: " + color + "; top: "+ top +"px; left: " + left + "px;'></div>";	
	return parseInt(uniqueClickId);
}

function displayLoadingScreen() {
	element("loading-screen").style.display = "block";
	element("loading-text").style.display = "block";
}

function hideLoadingScreen() {
	element("loading-screen").style.display = "none";
	element("loading-text").style.display = "none";
}

function handleResize() {
	saveWindowSize();
	var points = document.getElementsByClassName("point");
	for(var i = 0; i < points.length; i++) {
		movePoint(points.item(i));
	}
}

function movePoint(point) {
	if(prevWindowHeight == null) {
		return;
	}
	var newTop = parseInt(point.style.top.substring(0, point.style.top.length - 2)) + ((currWindowHeight - prevWindowHeight)/2) + "px";
	var newLeft = parseInt(point.style.left.substring(0, point.style.left.length - 2)) + ((currWindowWidth - prevWindowWidth)/2) + "px";
	point.style.top = newTop;
	point.style.left = newLeft;
}

function saveWindowSize() {
	prevWindowWidth = currWindowWidth;
	prevWindowHeight = currWindowHeight;
	currWindowWidth = window.innerWidth;
	currWindowHeight = window.innerHeight;
}

function displaySteps(steps) {	
	directions = steps;
	directionNumber = 0;
	updateStep(0);
	element("steps-back").onclick = function() {
		updateStep(-1);
	}
	element("steps-forward").onclick = function() {
		updateStep(1);
	}
}

function updateStep(increment) {
	var holder = element("steps-text");
	var possibleIndex = directionNumber + increment; 
	if (directions.length > possibleIndex && possibleIndex >= 0) {
		directionNumber = possibleIndex;
		holder.innerHTML = "";
		holder.innerHTML = directions[directionNumber];
	}
}

function swapImages(newNames) {
	var images = document.getElementsByClassName("floor");
	for(var i = 0; i < images.length; i++) {
		var original = images.item(i).dataset.original;
		images.item(i).src = newNames[i];
	}
}

function sendData() {
	// https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/send
	var xhr = new XMLHttpRequest();
	xhr.open('POST', '/uploadPoints', true);
	xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	xhr.onload = function (response) {
		handleResponse(response);
	};
	var data = JSON.stringify({pointA: pointA, pointB: pointB, excludeList: excludePoints});
	xhr.send(data);
}

function getClickInformation(event) {
	var imageElem = event.srcElement;
	var fileName = imageElem.currentSrc;
	var displayWidth = imageElem.width;
	var displayHeight = imageElem.height;
	var naturalWidth = imageElem.naturalWidth;
	var naturalHeight = imageElem.naturalHeight;
	var clientOffsetX = event.offsetX;
	var clientOffsetY = event.offsetY;
	
	var actualX = Math.round((clientOffsetX / displayWidth) * naturalWidth);
	var actualY = Math.round((clientOffsetY / displayHeight) * naturalHeight);
	
	var information = {
			"filename" : fileName,
			"xcoord" : actualX,
			"ycoord" : actualY,
			"offsetX" : clientOffsetX,
			"offsetY" : clientOffsetY,
			"displayWidth" : displayWidth,
			"displayHeight" : displayHeight,
			"naturalWidth" : naturalWidth,
			"naturalHeight" : naturalHeight
		};
	return information;
}

function changeCursor(value) {
	document.body.style.cursor = value;	
}

function getFloors() {
	return document.getElementsByClassName("floor");
}

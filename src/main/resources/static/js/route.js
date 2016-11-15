var pointA = null;
var pointB = null;
var excludePoints = [];
var currWindowWidth = null;
var currWindowHeight = null;
var prevWindowWidth = null;
var prevWindowHeight = null;
var END_POINT_COLOR = "#33C3F0"; // From skeleton.css to match theme.
var EXCLUDE_POINT_COLOR = "red";

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
	removeClickHandlersForExcluding();
	addClickHandlersForPicking();
	selectPickPointsButton();
	changeCursor("pointer");
}

function excludePointsMode() {
	removeClickHandlersForPicking();
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

function removeClickHandlersForPicking() {
	setClickHandlersOnFloors(null);
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

function removeClickHandlersForExcluding() {
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
}

function selectPickPointsButton() {
	greyButton("pick");
	whiteButton("exclude");
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

function displayClick(event, color) {
	var holder = element("point-holder");
	var margin = 15;
	var top = (event.pageY - margin);
	var left = Math.round(event.pageX - holder.getBoundingClientRect().left - margin);
	holder.innerHTML += "<div class='point' data-color='" + color + "' style='background: " + color + "; top: "+ top +"px; left: " + left + "px;'></div>";
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

var directions;
var directionNumber = 0;

function displaySteps(steps) {
	directions = steps;
	directionNumber = 0;
	updateStep();
	element("steps-holder").onclick = function() {
		updateStep();
	}
}

function updateStep() {
	var holder = element("steps-holder");
	holder.innerHTML = "";
	if (directions.length > directionNumber) {
		holder.innerHTML += directions[directionNumber];
	}
	directionNumber = directionNumber + 1;
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
	
	var information = {"filename" : fileName, "xcoord" : actualX, "ycoord" : actualY};
	return information;
}

function changeCursor(value) {
	document.body.style.cursor = value;	
}

function getFloors() {
	return document.getElementsByClassName("floor");
}
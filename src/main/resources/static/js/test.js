document.addEventListener("DOMContentLoaded", function() {
	QUnit.test("main-element", function(assert) {
		assert.ok(element("test-div").dataset.text == "This is a test.");
	});
	
	QUnit.test("route-truncate", function(assert) {
		assert.ok(truncate(4.5) == 4);
		assert.ok(truncate(4.7) == 4);
		assert.ok(truncate(4.2) == 4);
		assert.ok(truncate(4) == 4);
		assert.ok(truncate(-4.2) == -4);		
	});

	
	QUnit.test("route-getFloors", function(assert) {
		var expected = [].length;
		var actual = getFloors().length;
		assert.ok(expected == actual);
	});
	
	QUnit.test("route-changeCursor", function(assert) {
		var expected = "crosshair";
		changeCursor(expected);
		var actual = document.body.style.cursor;
		assert.ok(expected == actual);
		changeCursor("pointer");
	});
	
	QUnit.test("route-getClickInformation", function(assert) {
		var click = eventFire(document.body, 'click');
		var expected = click.offsetX;
		var actual = getClickInformation(click).offsetX;
		assert.ok(expected == actual);
	});
	
	QUnit.test("route-sendData", function(assert) {
		var dataSent = sendData(true);
		assert.ok('{"pointA":null,"pointB":null,"excludeList":[]}' == dataSent);
		pointA = {testObj : "thing"};
		dataSent = sendData(true);
		assert.ok('{"pointA":{"testObj":"thing"},"pointB":null,"excludeList":[]}' == dataSent);
	});
	
	QUnit.test("route-swapImages", function(assert) {
		var image1 = document.createElement("img");
		image1.src = "old1";
		var image2 = document.createElement("img");
		image1.src = "old2";
		var images = [image1, image2];
		var newNames = [];
		newNames[0] = "new1";
		newNames[1] = "new2";
		swapImages(images, newNames);
		assert.ok(image1.src.endsWith("new1"));
		assert.ok(image2.src.endsWith("new2"));
	});
	
	QUnit.test("route-updateStep", function(assert) {
		var expected = "A very important step.";
		directions = [expected];
		directionNumber = -1;
		var holder = document.createElement("div");
		updateStepHelper(holder, 1);
		var actual = holder.innerHTML;
		assert.ok(expected == actual);
		updateStepHelper(holder, 1);
		assert.ok(expected == actual);
		updateStepHelper(holder, -1);
		assert.ok(expected == actual);
	});
	
	QUnit.test("route-saveWindowSize", function(assert) {
		var expected = window.innerWidth;
		var expected2 = window.innerHeight;
		saveWindowSize();
		var actual = currWindowWidth;
		var actual2 = currWindowHeight;
		assert.ok(expected == actual);
		assert.ok(expected2 == actual2);
		saveWindowSize();
		assert.ok(prevWindowWidth == expected);
		assert.ok(prevWindowHeight == expected2);
	});
	
	QUnit.test("route-movePoint", function(assert) {
		var point = document.createElement("div");
		var expected = point.style.top;
		eventFire(document.body, "resize");
		eventFire(document.body, "resize");
		movePoint(point);
		var actual = point.style.top;
		assert.ok(expected == actual);
	});
	
	QUnit.test("route-hideLoadingScreen", function(assert) {
		var expected = "none";
		hideLoadingScreen();
		var actual = element("loading-screen").style.display;
		assert.ok(expected == actual);
		var actual2 = element("loading-text").style.display;
		assert.ok(expected == actual2);
	});
	
	QUnit.test("route-showLoadingScreen", function(assert) {
		var expected = "block";
		showLoadingScreen();
		var actual = element("loading-screen").style.display;
		assert.ok(expected == actual);
		var actual2 = element("loading-text").style.display;
		assert.ok(expected == actual2);
	});
	
	QUnit.test("route-displayClickFromPoint", function(assert) {
		var expected = "red";
		var id = displayClickFromPoint(45, 40, "red");
		var actual = element(id).dataset.color;
		assert.ok(expected == actual);	
		var expectedTop = "25px";
		assert.ok(expectedTop == element(id).style.top);
		var left = Math.round(45 - element("point-holder").getBoundingClientRect().left - 15);
		var expectedLeft = left + "px";
		assert.ok(expectedLeft == element(id).style.left);
	});
	
	QUnit.test("route-updateAnnotationList", function(assert) {
		var select = document.createElement("select");
		var annotation = {title : "test"};
		updateAnnotationsList(select, annotation, 0);
		var expected = 1;
		var actual = select.options.length;
		assert.ok(expected == actual);		
	});
	
	QUnit.test("route-finalizeAnnotation", function(assert) {
		var corners = [{id: 1, data: {xcoord: 5, ycoord: 10}}];
		var annotation = {title: "test", corners : corners};
		var expected = 5;
		finalizeAnnotation(annotation);
		var actual = annotation.avgX;
		assert.ok(expected == actual);
		var expectedY = 10;
		var actualY = annotation.avgY;
		assert.ok(expectedY == actualY);
	});
	
	QUnit.test("route-resetPoints-true", function(assert) {
		removeElementsByClass("point");
		var expected = 2;
		displayClickFromPoint(5, 5, EXCLUDE_POINT_COLOR);
		displayClickFromPoint(15, 35, END_POINT_COLOR);
		displayClickFromPoint(45, 25, END_POINT_COLOR);
		resetPoints(true);
		var actual = document.getElementsByClassName("point").length;
		assert.ok(expected == actual);
	});
	
	QUnit.test("route-resetPoints-false", function(assert) {
		removeElementsByClass("point");
		var expected = 1;
		displayClickFromPoint(5, 5, EXCLUDE_POINT_COLOR);
		displayClickFromPoint(15, 35, END_POINT_COLOR);
		displayClickFromPoint(45, 25, END_POINT_COLOR);
		resetPoints(false);
		var actual = document.getElementsByClassName("point").length;
		assert.ok(expected == actual);
	});
	
	QUnit.test("route-excludePoint", function(assert) {
		var expected = 1;
		var dummy = {};
		var srcElement = {};
		srcElement.width = 23;
		srcElement.naturalWidth = 2323;
		dummy.srcElement = srcElement;
		dummy.offsetX = 23;
		excludePoint(dummy);
		var actual = excludePoints.length;
		assert.ok(expected == actual);		
	});
	
	QUnit.test("route-addPoint", function(assert) {
		var dummy = {};
		var srcElement = {};
		srcElement.width = 23;
		srcElement.naturalWidth = 2323;
		dummy.srcElement = srcElement;
		dummy.offsetX = 23;
		pointA = null;
		pointB = null;
		assert.ok(pointA == null);
		assert.ok(pointB == null);
		addPoint(dummy);
		assert.ok(pointA != null);
		addPoint(dummy);
		assert.ok(pointB != null);
	});
	
	QUnit.test("route-setClickHandlersOnFloors", function(assert) {
		var testFunction = function() {alert("Should never see this.")};
		var element = document.createElement("div");
		element.className = "floor";
		document.body.append(element);
		setClickHandlersOnFloors(testFunction);
		var floors = getFloors();
		for (var i = 0; i < floors.length; i++) {
			assert.ok(floors[i].onclick == testFunction);
		}
	});
	
	QUnit.test("route-removeClickHandlers", function(assert) {
		var element = document.createElement("div");
		element.className = "floor";
		document.body.append(element);
		removeClickHandlers();
		var floors = getFloors();
		for (var i = 0; i < floors.length; i++) {
			assert.ok(floors[i].onclick == null);
		}
	});	
	
	// http://stackoverflow.com/questions/2705583/how-to-simulate-a-click-with-javascript
	function eventFire(el, etype){
		if (el.fireEvent) {
			el.fireEvent('on' + etype);
		} else {
			var evObj = document.createEvent('Events');
			evObj.initEvent(etype, true, false);
			el.dispatchEvent(evObj);
			return evObj;
		}
	}
	
	// http://stackoverflow.com/questions/4777077/removing-elements-by-class-name
	function removeElementsByClass(className){
	    var elements = document.getElementsByClassName(className);
	    while(elements.length > 0){
	        elements[0].parentNode.removeChild(elements[0]);
	    }
	}

});
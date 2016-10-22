detector = vision.CascadeObjectDetector('ElevatorDetector.xml');
%to change which picture you want to find elevators in, change the img to
%Glennan\Glennan1.jpg or any of the other jpgs in the Glennan folder
img = imread('NoDoors.png');
bbox = step(detector,img);
detectedImg = insertObjectAnnotation(img,'rectangle',bbox,'elevator');
figure; imshow(detectedImg);
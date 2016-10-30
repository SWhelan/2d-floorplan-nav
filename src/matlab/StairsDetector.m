detector = vision.CascadeObjectDetector('StairsDetector.xml');
img = imread('Glennan\Glennan2.jpg');
bbox = step(detector,img);
detectedImg = insertObjectAnnotation(img,'rectangle',bbox,'stairs');
figure; imshow(detectedImg);
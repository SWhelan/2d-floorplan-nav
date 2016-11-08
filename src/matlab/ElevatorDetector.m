function ElevatorDetector
    detector = vision.CascadeObjectDetector('ElevatorDetector.xml');
    img = imread('Glennan\Glennan3.jpg');   
    bbox = step(detector,img);
    %this is for the elevator removal
%     for i=1:length(bbox)
%         centerx = bbox(i,1)+bbox(i,3)/2;
%         centery = bbox(i,2)+bbox(i,4)/2;
%         RedFinder(centerx,centery,img);
%     end
    detectedImg = insertObjectAnnotation(img,'rectangle',bbox,'elevator');
    figure; imshow(detectedImg);
end
    
% function RedFinder(x,y,img)
%     if img(x,y,:) == 255
%         RedFinder(x,y+1,img);
%     end
%     RedRemover(x,y,img);
% end
% 
% function RedRemover(x,y,img)
%     if show
%         img(x,y,:) = 225;
%     end
%     x=x+1;
%     if img(x,y,:) ~= 255
%         RedRemover(x,y,img);
%     end
%     x=x-2;
%     if img(x,y,:) ~= 255
%         RedRemover(x,y,img);
%     end
%     y=y+1;
%     if img(x,y,:) ~= 255
%         RedRemover(x,y,img);
%     end
%     y=y-2;
%     if img(x,y,:) ~= 255
%         RedRemover(x,y,img);
%     end
% end



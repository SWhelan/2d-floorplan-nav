function ElevatorDetector
    detector = vision.CascadeObjectDetector('ElevatorDetector.xml');
    img = imread('Glennan/Glennan2.jpg');   
    display=img;
    bbox = step(detector,img);
    %this is for the elevator removal. Recomended to see what happens with
    %just one elevator is comment out the loop and replace the 'i's with 1
    for i=1:size(bbox,1)
        centerx = bbox(i,1)+bbox(i,3)/2;  
        centery = bbox(i,2)+bbox(i,4)/2;
        finder = RedRemover(centery,centerx,display); 
% I realize now that because it loops the finder will be replaced with the 
% second finder. I dont know how I should/could add the images together
    end
    detectedImg = insertObjectAnnotation(img,'rectangle',bbox,'elevator');
    figure; imshow(finder);
end
    
function finder = RedFinder(y,x,img)
    if impixel(img,y,x) == [255 255 255] 
        y=y+1;
        finder = RedFinder(y,x,img);
    else
        finder = RedRemover(y,x,img);
    end
end

function remover = RedRemover(y,x,img)
    img(y,x,:) = 255;
    remover=img;
    if impixel(img,y,x+1) ~= [255 255 255]
        remover = RedRemover(y,x+1,img);
    end
    if impixel(img,y,x-1) ~= [255 255 255]
        remover = RedRemover(y,x-1,img);
    end
    if impixel(img,y+1,x) ~= [255 255 255]
        remover = RedRemover(y+1,x,img);
    end
    if impixel(img,y-1,x) ~= [255 255 255]
        remover = RedRemover(y-1,x,img);
    end
    if impixel(img,y+1,x+1) ~= [255 255 255]
        remover = RedRemover(y,x+1,img);
    end
    if impixel(img,y-1,x-1) ~= [255 255 255]
        remover = RedRemover(y,x-1,img);
    end
    if impixel(img,y+1,x-1) ~= [255 255 255]
        remover = RedRemover(y+1,x,img);
    end
    if impixel(img,y-1,x+1) ~= [255 255 255]
        remover = RedRemover(y-1,x,img);
    end
end



%read the image pointed to by file witha dot spacing of width
function DotGrid (file, width)
image = imread(file);
display = image;
rowBound = size(display, 1);
colBound = size(display, 2);
for r=floor(width/2):width:rowBound
   for c=floor(width/2):width:colBound
      if display(r,c,:) == 255
          display(r,c,:) = 100;
          if (r+width <= rowBound) & (display(r+1:r+width-1, c,:) == 255)
             display(r+1:r+width-1, c, :) = 100; 
          end
          if (c+width <= colBound) & (display(r, c+1:c+width-1,:) == 255)
             display(r, c+1:c+width-1, :) = 100; 
          end
      end
   end
end
imshow(display)
%read the image pointed to by file witha dot spacing of width
function DotGrid (file, width)
image = imread(file);
display = image;
rowBound = size(display, 1);
rowLength = floor(rowBound/width);
colBound = size(display, 2);
colLength = floor(colBound/width);
%adjacency list is rxcx2
AdjacencyMatrix = cell(rowLength, colLength);
for r=1:rowLength
   for c=1:colLength
      rPixel = r*width;
      cPixel = c*width;
      if display(rPixel,cPixel,:) == 255
          %color the dot
          display(rPixel,cPixel,:) = 0;
          %if the next horizontal point is in bounds and the image is clear between
          %them, add the adjacency to both points
          if (c+1 <= colLength) & (display(rPixel, cPixel+1:cPixel+width-1,:) == 255)
             AdjacencyMatrix{r, c} = [AdjacencyMatrix{r,c} [r c+1]];
             AdjacencyMatrix{r,c+1} = [AdjacencyMatrix{r,c+1} [r c]];
             display(rPixel, cPixel+1:cPixel+width-1,1) = 131;
             display(rPixel, cPixel+1:cPixel+width-1,2) = 255;
             display(rPixel, cPixel+1:cPixel+width-1,3) = 119;
          end
          %if the next vertical point is in bounds and the image is clear between
          %them, add the adjacency to both points
          if (r+1 <= rowLength) & (display(rPixel+1:rPixel+width-1, cPixel,:) == 255)
             AdjacencyMatrix{r,c} = [AdjacencyMatrix{r,c} [r+1 c]];
             AdjacencyMatrix{r+1,c} = [AdjacencyMatrix{r+1,c} [r c]];
             display(rPixel+1:rPixel+width-1, cPixel,1) = 131;
             display(rPixel+1:rPixel+width-1, cPixel,2) = 255;
             display(rPixel+1:rPixel+width-1, cPixel,3) = 119;
          end
      end
   end
end
imshow(display)
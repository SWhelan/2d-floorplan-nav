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
end

function r = Heuristic(start, goal)
r = abs(start(1)-goal(1)) + abs(start(2)-goal(2));
end

%Takes adjacency matrix and two ordered pairs start, end
function AStarSearch(AdjacencyMatrix, start, goal)
%[cost, heuristic value, function value, parent x, parent y]
SearchMatrix = cell(size(AdjacencyMatrix, 1), size(AdjacencyMatrix,2));
%initialize starting location
SearchMatrix{start(1), start(2), 1} = 0;
SearchMatrix{start(1), start(2), 2} = Heuristic(start, goal);
SearchMatrix{start(1), start(2), 3} = Heuristic(start, goal);
%TODO: learn how to add a comparator to the priority queue
OpenList = java.util.PriorityQueue();
ClosedList = java.util.LinkedList();
OpenList.add(SearchMatrix{start(1), start(2)});
Path = [];
while OpenList.size()>0
    if ExploreLocation(SearchMatrix, AdjacencyMatrix, goal, OpenList, ClosedList)
       %create the path
       break;
    end
end
if size(Path,1)>0
   %do good things
else
   %do bad things 
end
end

function goalReached = ExploreLocation(SearchMatrix, AdjacencyMatrix, goal, OpenList, ClosedList)
goalReached = false;
location = OpenList.poll();
for i=1:AdjacencyMatrix{location(0), location(1),:}
   if CheckLocation(location, SearchMatrix, AdjacencyMatrix, goal, OpenList, ClosedList)
       goalReached=true;
   end
end
end

function goalReached = CheckLocation(location, SearchMatrix, AdjacencyMatrix, goal, OpenList, ClosedList)
goalReached = location(1) == goal(1) && location(2) == goal(2);
valid = true;
%check closed list
for i=1:ClosedList.size()
   check = ClosedList.get(i);
   if location(1)==check(1) && location(2)==check(2)
      valid = false; 
   end
end
%check open list
if valid
   check = OpenList.get(i);
   for i=1:OpenList.size()
      if location(1)==check(1) && location(2)==check(2)
          valid = false;
      end
   end
end
%add to open list
if valid
       
end
end

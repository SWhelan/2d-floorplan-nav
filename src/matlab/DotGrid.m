
function adjacencyMatrix = DotGrid (file, width)    
    image = imread(file);    
    imshow(image)
    [x,y] = ginput(2);
    x = round(x/width)
    y = round(y/width)
    adjacencyMatrix = FindConnections(file, width);
    aStarAgent = javaObjectEDT('Pathfinding.AStar');
    path = javaMethod('AStarSearch', aStarAgent, [x(1) y(1)], [x(2) y(2)], adjacencyMatrix)
    DisplayPath(image, width, path);
    exit
end

function DisplayPath(image, width, path)
    for i=1:size(path,1)-1
        p1 = path(i,:);
        p2 = path(i+1,:);
        x1 = min([p1(1) p2(1)]);
        x2 = max([p1(1) p2(1)]);
        y1 = min([p1(2) p2(2)]);
        y2 = max([p1(2) p2(2)]);
        image(y1*width:y2*width, x1*width:x2*width, 1) = 77;
        image(y1*width:y2*width, x1*width:x2*width, 2) = 56;
        image(y1*width:y2*width, x1*width:x2*width, 3) = 153;
    end
    %figure
    imshow(image)
end

%read the image pointed to by file with a dot spacing of width
function adjacencyMatrix = FindConnections (file, width)
image = imread(file);
display = image;
yBound = size(display, 1);%rows
yLength = floor(yBound/width);
xBound = size(display, 2);%columns
xLength = floor(xBound/width);
adjacencyMatrix = javaObjectEDT('Pathfinding.AdjacencyMatrix', xBound, yBound);
for y=1:yLength
   for x=1:xLength
      xPixel = x*width;
      yPixel = y*width;
      if display(yPixel,xPixel,:) == 255
          %color the dot black
          display(yPixel,xPixel,:) = 0;
          %if the next horizontal point is in bounds and the image is clear between
          %them, add the adjacency to both points
          if (x+1 <= xLength) & (display(yPixel, xPixel+1:xPixel+width-1,:) == 255)
              javaMethod('addAdjacency', adjacencyMatrix,[x-1 y-1], [x+1-1 y-1]);%account for 1->0 indexing
              display(yPixel, xPixel+1:xPixel+width-1,1) = 131;
              display(yPixel, xPixel+1:xPixel+width-1,2) = 255;
              display(yPixel, xPixel+1:xPixel+width-1,3) = 119;
          end
          %if the next vertical point is in bounds and the image is clear between
          %them, add the adjacency to both points
          if (y+1 <= yLength) & (display(yPixel+1:yPixel+width-1, xPixel,:) == 255)
              javaMethod('addAdjacency', adjacencyMatrix, [x-1 y-1], [x-1 y+1-1]);%account for 1->0 indexing
              display(yPixel+1:yPixel+width-1, xPixel,1) = 131;
              display(yPixel+1:yPixel+width-1, xPixel,2) = 255;
              display(yPixel+1:yPixel+width-1, xPixel,3) = 119;
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

function path = DotGrid (file, width, show)
    image = imread(file);    
    imshow(image)
    [x,y] = ginput(2);
    x = round(x/width);
    y = round(y/width);
    adjacencyMatrix = FindConnections(file, width, show);
    aStarAgent = javaObjectEDT('Pathfinding.AStar');
    path = javaMethod('AStarSearch', aStarAgent, [x(1) y(1)], [x(2) y(2)], adjacencyMatrix)
    SavePath(file, image, width, path, show);
    formatSpec = '(%u, %u), ';
    writeFile = fopen('path.txt', 'w');
    fprintf(writeFile, formatSpec, transpose(path));
    fclose(writeFile);
end

function FindPath(x1, y1, z1, x2, y2, z2, file)
    width = 36;
    show = 0;
    image = imread(file);
    x = [x1 x2];
    y = [y1 y2];
    x = round(x/width);
    y = round(y/width);
    adjacencyMatrix = FindConnections(file, width, show);
    aStarAgent = javaObjectEDT('Pathfinding.AStar');
    path = javaMethod('AStarSearch', aStarAgent, [x(1) y(1)], [x(2) y(2)], adjacencyMatrix);
    SavePath(file, image, width, path, show);
    formatSpec = '(%u, %u), ';
    writeFile = fopen('path.txt', 'w');
    fprintf(writeFile, formatSpec, transpose(path));
    fclose(writeFile);
end

function SavePath(file, image, width, path, show)
    for i=1:size(path,1)-1
        p1 = path(i,:);
        p2 = path(i+1,:);
        xDir = p2(1) - p1(1);
        xDir = xDir./abs(xDir);
        yDir = p2(2) - p1(2);
        yDir = yDir./abs(yDir);
        xPixel = p1(1)*width;
        yPixel = p1(2)*width;
        for i=1:width
            image(yPixel-1:yPixel+1, xPixel-1:xPixel+1, 1) = 77;
            image(yPixel-1:yPixel+1, xPixel-1:xPixel+1, 2) = 56;
            image(yPixel-1:yPixel+1, xPixel-1:xPixel+1, 3) = 153;
            yPixel = yPixel + yDir;
            xPixel = xPixel + xDir;
        end
    end
    filename = [file(1:size(file,2)-4) '_path.jpg'];
    imwrite(image, filename);
    if show
        figure
        imshow(image)
    end
end

%read the image pointed to by file witha dot spacing of width
function adjacencyMatrix = FindConnections (file, width, show)
    image = imread(file);
    if show
        display = image;
    end
    yBound = size(image, 1);%rows
    yLength = floor(yBound/width);
    xBound = size(image, 2);%columns
    xLength = floor(xBound/width);
    adjacencyMatrix = javaObjectEDT('Pathfinding.AdjacencyMatrix', xBound, yBound);
    for y=1:yLength
       for x=1:xLength
          xPixel = x*width;
          yPixel = y*width;
          if image(yPixel,xPixel,:) == 255
              %color the dot black
              if show
                display(yPixel,xPixel,:) = 0;
              end
              %Check east, southeast, south, southwest points
                %first check point is in bounds then check that the pixels
                %between the points are clear
              %east
              if (x+1 <= xLength) & (image(yPixel, xPixel+1:xPixel+width-1,:) == 255)
                  javaMethod('addAdjacency', adjacencyMatrix,[x y]-1, [x+1 y]-1);%account for 1->0 indexing
                  if show
                      display(yPixel, xPixel+1:xPixel+width-1,1) = 131;
                      display(yPixel, xPixel+1:xPixel+width-1,2) = 255;
                      display(yPixel, xPixel+1:xPixel+width-1,3) = 119;
                  end
              end
              %southeast
              if (x+1 <= xLength) & (y+1 <= yLength) & CheckDiagonal([y x], [y+1 x+1], image, width)
                  javaMethod('addAdjacency', adjacencyMatrix,[x y]-1, [x+1 y+1]-1);%account for 1->0 indexing
                  if show
                      r = yPixel+1;
                      c = xPixel+1;
                      for i=1:width-1
                          display(r,c,1)= 131;
                          display(r,c,2)= 255;
                          display(r,c,3)= 119;
                          r = r+1;
                          c = c+1;
                      end
                  end
              end
              %south
              if (y+1 <= yLength) & (image(yPixel+1:yPixel+width-1, xPixel,:) == 255)
                  javaMethod('addAdjacency', adjacencyMatrix, [x y]-1, [x y+1]-1);%account for 1->0 indexing
                  if show
                      display(yPixel+1:yPixel+width-1, xPixel,1) = 131;
                      display(yPixel+1:yPixel+width-1, xPixel,2) = 255;
                      display(yPixel+1:yPixel+width-1, xPixel,3) = 119;
                  end
              end
              %southwest
              if (x-1 >= 1) & (y+1 <= yLength) & CheckDiagonal([y x], [y+1 x-1], image, width)
                  javaMethod('addAdjacency', adjacencyMatrix,[x y]-1, [x-1 y+1]-1);%account for 1->0 indexing
                  if show
                      r = yPixel+1;
                      c = xPixel-1;
                      for i=1:width-1
                          display(r,c,1)= 131;
                          display(r,c,2)= 255;
                          display(r,c,3)= 119;
                          r = r+1;
                          c = c-1;
                      end
                  end
              end
          end
       end
    end
    if show
        imshow(display)
    end
end

function valid = CheckDiagonal(point1, point2, image, width)
    yDir = point2(1) - point1(1);
    yDir = yDir./abs(yDir);
    xDir = point2(2) - point1(2);
    xDir = xDir./abs(xDir);
    valid = 1;
    yPixel = point1(1)*width+yDir;
    xPixel = point1(2)*width+xDir;
    for i=1:width-1
        valid = valid & image(yPixel,xPixel,:)==255;
        yPixel = yPixel + yDir;
        xPixel = xPixel + xDir;
    end
end
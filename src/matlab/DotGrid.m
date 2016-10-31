function DotGrid(x1, y1, z1, x2, y2, z2)
    InitClasspath();
    files = GetFilenames();
    file = files{1};
    width = 36;
    show = 0;
    image = imread(file);
    x = [x1 x2];
    y = [y1 y2];
    z = [z1 z2];
    x = round(x/width);
    y = round(y/width);
    adjacencyMatrix = ConnectBuilding(files, width, show);
    aStarAgent = javaObjectEDT('Pathfinding.AStar');
    path = javaMethod('aStarSearch', aStarAgent, [x(1) y(1) z(1)]-1, [x(2) y(2) z(2)]-1, adjacencyMatrix);
    SavePath(files, width, path, show);
    formatSpec = '%u,%u\n';
    writeFile = fopen('path.txt', 'w');
    fprintf(writeFile, formatSpec, transpose(path));
    fclose(writeFile);
    exit;
end

function InitClasspath ()
    classpathData = ReadFile('javaclasspath.txt');
    javaaddpath(classpathData{1});
end

function path = DotGrid2 (files, width, show)
    x = [];
    y = [];
    z = [];
    for i=1:size(files,2)
        image = imread(files{i});
        imshow(image);
        title(files{i});
        [a,b] = ginput();
        x = [x a];
        y = [y b];
        for n=1:size(a, 1)
            z = [z i];
        end
    end
    close all;
    x = round(x/width);
    y = round(y/width);
    adjacencyMatrix = ConnectBuilding(files, width, show);
    aStarAgent = javaObjectEDT('Pathfinding.AStar');
    path = javaMethod('aStarSearch', aStarAgent, [x(1) y(1) z(1)]-1, [x(2) y(2) z(2)]-1, adjacencyMatrix)
    SavePath(files, width, path, show);
    formatSpec = '(%u, %u), ';
    writeFile = fopen('path.txt', 'w');
    fprintf(writeFile, formatSpec, transpose(path));
    fclose(writeFile);
end

function filenames = GetFilenames()
    filenames = ReadFile('filenames.txt');
end

function data = ReadFile(filename)
    fileId = fopen(filename, 'r');
    data = cell(0,1);
    line = fgetl(fileId);
    while ischar(line)
        data{end + 1, 1} = line;
        line = fgetl(fileId);
    end
    fclose(fileId);
end

function SavePath(files, width, path, shows)
    images = cell(size(files, 2), 1);
    for i=1:size(files,2)
        images{i} = imread(files{1});
    end
    for i=1:size(path,1)-1
        p1 = path(i,:);
        p2 = path(i+1,:);
        if p1(3) == p2(3)
            xDir = p2(1) - p1(1);
            xDir = xDir./abs(xDir);
            yDir = p2(2) - p1(2);
            yDir = yDir./abs(yDir);
            xPixel = p1(1)*width;
            yPixel = p1(2)*width;
            for i=1:width
                images{p1(3)}(yPixel-1:yPixel+1, xPixel-1:xPixel+1, 1) = 77;
                images{p1(3)}(yPixel-1:yPixel+1, xPixel-1:xPixel+1, 2) = 56;
                images{p1(3)}(yPixel-1:yPixel+1, xPixel-1:xPixel+1, 3) = 153;
                yPixel = yPixel + yDir;
                xPixel = xPixel + xDir;
            end
        end
    end
    for i=1:size(files,2)
       filename = [files{i}(1:size(files{i}, 2)-4) '_path.jpg'];
       imwrite(images{i}, filename);
       if shows
            figure;
            imshow(images{i});
            title(filename);
       end
    end
end

function adjacencyMatrix = ConnectBuilding (files, width, show)
    floors = size(files, 2);
    adjacencyMatrix = javaObjectEDT('Pathfinding.AdjacencyMatrix', floors);
    for f=1:floors
        ConnectFloor(files{f}, width, f, adjacencyMatrix, show);
    end
end

%read the image pointed to by file with a dot spacing of width
function ConnectFloor (file, width, f, adjacencyMatrix, show)
    image = imread(file);
    if show
        display = image;
    end
    yBound = size(image, 1);%rows
    yLength = floor(yBound/width);
    xBound = size(image, 2);%columns
    xLength = floor(xBound/width);
    javaMethod('createFloor', adjacencyMatrix, xBound, yBound);
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
                  javaMethod('addAdjacency', adjacencyMatrix,[x y f]-1, [x+1 y f]-1);%account for 1->0 indexing
                  if show
                      display(yPixel, xPixel+1:xPixel+width-1,1) = 131;
                      display(yPixel, xPixel+1:xPixel+width-1,2) = 255;
                      display(yPixel, xPixel+1:xPixel+width-1,3) = 119;
                  end
              end
              %southeast
              if (x+1 <= xLength) & (y+1 <= yLength) & CheckDiagonal([y x], [y+1 x+1], image, width)
                  javaMethod('addAdjacency', adjacencyMatrix,[x y f]-1, [x+1 y+1 f]-1);%account for 1->0 indexing
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
                  javaMethod('addAdjacency', adjacencyMatrix, [x y f]-1, [x y+1 f]-1);%account for 1->0 indexing
                  if show
                      display(yPixel+1:yPixel+width-1, xPixel,1) = 131;
                      display(yPixel+1:yPixel+width-1, xPixel,2) = 255;
                      display(yPixel+1:yPixel+width-1, xPixel,3) = 119;
                  end
              end
              %southwest
              if (x-1 >= 1) & (y+1 <= yLength) & CheckDiagonal([y x], [y+1 x-1], image, width)
                  javaMethod('addAdjacency', adjacencyMatrix,[x y f]-1, [x-1 y+1 f]-1);%account for 1->0 indexing
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
        figure;
        imshow(display);
        title(file);
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


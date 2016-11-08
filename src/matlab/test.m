%recurse is whether or not to use the recursive remover
function test(file, x, y, recurse)
    im = imread(file);
    tic
    for i=1:size(x,2)
       im = RedFinder(x(i), y(i), im, recurse); 
    end
    toc
    figure
    imshow(im)
end

%entry level remover method, finds red spot to start removing from
function finder = RedFinder(x,y,img, recurse)
    while all(img(y,x,:) == 255)
        y=y+1;
    end
    if (recurse)
        finder = RedRemover(x,y,img);
    else
        finder = AltRemover(x,y,img);
    end
end

%removes red pixels recursively: very legit, super slow
function r = RedRemover(x,y,img)
    if all(img(y,x,:) == 255)
       r = img; 
    else
       r = img;
       r(y,x,:)=255;
       r = RedRemover(x+1,y,r);
       r = RedRemover(x-1,y,r);
       r = RedRemover(x,y+1,r);
       r = RedRemover(x,y-1,r);
    end
end

%removes by iteratively finding the corners of the elevator then painting
%that box white: little sketch in places, WAAAAY faster than recursion
function r = AltRemover(x,y,img)
    r = img;
    %threshold out noise by walls
    t = img>245 & img<255;
    img(t)=255;
    %depending on where in the red we start, we may need to change
    %directions once to find the top left
    p = FindTopRight(x,y,img);
    p = FindTopLeft(p(1),p(2),img);
    %find bottom right by going down then right as far as possible
    x = p(1);
    y = p(2);
    while any(img(y+1,x,:)~=255)
       y = y+1; 
    end
    while any(img(y,x+1,:)~=255)
       x = x+1; 
    end
    %paint the region white with an extra pixel on each side for safety
        %corners on picture may be rounded, so our corners might not be the
        %actual corners
    r(p(2)-1:y+1,p(1)-1:x+1,:) = 255;
end

%heads as far left and up as it can
function point = FindTopLeft(x,y,img)
    %get to top left corner
    while any(img(y,x-1,:) ~= 255)
       while any(img(y,x-1,:) ~= 255)
          x = x-1; 
       end
       while any(img(y-1,x,:) ~= 255)
          y = y-1; 
       end
    end
    point = [x y];
end

%heads as far right and up as it can
function point = FindTopRight(x,y,img)
    %get to top right corner
    while any(img(y,x+1,:) ~= 255)
       while any(img(y,x+1,:) ~= 255)
          x = x+1; 
       end
       while any(img(y-1,x,:) ~= 255)
          y = y-1; 
       end
    end
    point = [x y];
end
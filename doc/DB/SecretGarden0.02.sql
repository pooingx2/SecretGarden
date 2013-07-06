
-- 디렉토리 조회 (유저 아이디)
select type, File.name, depth, parent 
from Directory , User , File   
where user_id = 'Test' && master = 'Test' &&  dir_id = root
ORDER BY depth;
 

-- 디렉토리 생성 (생성하고자 하는 폴더명, 상위 폴더명, 부모 깊이+1, 부모의 Root)
INSERT INTO File(file_id, type, name, depth, parent, root) VALUES (NULL, 'dir', 'adddirs', 'mydir',  2,  1);


-- 디렉토리 삭제 (삭제하고자 하는 폴더명, 깊이 정보)
SET SQL_SAFE_UPDATES=0;
DELETE FROM File WHERE name = 'adddirs' && metaPath = 2;
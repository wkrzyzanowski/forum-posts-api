-- SELECT 1;
INSERT INTO TOPICS(ID, UUID, AUTHOR_UUID, TITLE, CREATION_DATE, LAST_POST_DATE, IMPORTANT, ACTIVE)
VALUES (1, 'guid_topic_1', '795756fd-e6e3-4266-b8b1-95fa0d73418a', 'FIRST TOPIC', LOCALTIME(), LOCALTIME(), 0, 1),
       (2, 'guid_topic_2', '3b56a26b-b8b3-4ed8-9bf2-60c2b981213a', 'SECOND TOPIC', LOCALTIME(), LOCALTIME(), 0, 1);
INSERT INTO POSTS(ID, UUID, CREATION_DATE, CONTENT, AUTHOR_UUID, LIKES, DISLIKES, TOPIC_ID)
VALUES (1, 'guid_post_1', LOCALTIME(), 'Some post content 1', '3b56a26b-b8b3-4ed8-9bf2-60c2b981213a', 0, 0, 1),
       (2, 'guid_post_2', LOCALTIME(), 'Some post content 2', '795756fd-e6e3-4266-b8b1-95fa0d73418a', 0, 0, 1),
       (3, 'guid_post_3', LOCALTIME(), 'Some post content 3', '3b56a26b-b8b3-4ed8-9bf2-60c2b981213a', 0, 0, 2),
       (4, 'guid_post_4', LOCALTIME(), 'Some post content 4', '3b56a26b-b8b3-4ed8-9bf2-60c2b981213a', 0, 0, 2),
       (5, 'guid_post_5', LOCALTIME(), 'Some post content 5', '795756fd-e6e3-4266-b8b1-95fa0d73418a', 0, 0, 2);
-- INSERT INTO POSTS(ID,UUID,TOPIC_ID) VALUES(2,'guid_post_2',1);
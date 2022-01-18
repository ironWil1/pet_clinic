alter table comment
drop constraint fk_comment_user_entities,
add constraint fk_comment_user_entities
    foreign key (user_id)
        references user_entities;
package ru.practicum.user.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.processing.Generated;

@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {
    private static final long serialVersionUID = 903137340L;
    public static final QUser user = new QUser("user");
    public final StringPath email = this.createString("email");
    public final NumberPath<Integer> id = this.createNumber("id", Integer.class);
    public final StringPath name = this.createString("name");

    public QUser(String variable) {
        super(User.class, PathMetadataFactory.forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }
}
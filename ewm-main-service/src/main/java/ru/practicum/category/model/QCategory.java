package ru.practicum.category.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.processing.Generated;

@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {
    private static final long serialVersionUID = 823197404L;
    public static final QCategory category = new QCategory("category");
    public final NumberPath<Integer> id = this.createNumber("id", Integer.class);
    public final StringPath name = this.createString("name");

    public QCategory(String variable) {
        super(Category.class, PathMetadataFactory.forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }
}

package ru.practicum.event.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;

import javax.annotation.processing.Generated;

@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLocation extends EntityPathBase<Location> {
    private static final long serialVersionUID = 1516074437L;
    public static final QLocation location = new QLocation("location");
    public final NumberPath<Integer> id = this.createNumber("id", Integer.class);
    public final NumberPath<Float> lat = this.createNumber("lat", Float.class);
    public final NumberPath<Float> lon = this.createNumber("lon", Float.class);

    public QLocation(String variable) {
        super(Location.class, PathMetadataFactory.forVariable(variable));
    }

    public QLocation(Path<? extends Location> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLocation(PathMetadata metadata) {
        super(Location.class, metadata);
    }
}

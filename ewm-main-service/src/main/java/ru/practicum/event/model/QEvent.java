package ru.practicum.event.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.*;
import ru.practicum.assistant.State;
import ru.practicum.category.model.QCategory;
import ru.practicum.user.model.QUser;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;

@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvent extends EntityPathBase<Event> {
    private static final long serialVersionUID = -299444598L;
    private static final PathInits INITS;
    public static final QEvent event;
    public final StringPath annotation;
    public final QCategory category;
    public final NumberPath<Integer> confirmedRequests;
    public final DateTimePath<LocalDateTime> createdOn;
    public final StringPath description;
    public final DateTimePath<LocalDateTime> eventDate;
    public final NumberPath<Integer> id;
    public final QUser initiator;
    public final QLocation location;
    public final BooleanPath paid;
    public final NumberPath<Integer> participantLimit;
    public final DateTimePath<LocalDateTime> publishedOn;
    public final BooleanPath requestModeration;
    public final EnumPath<State> state;
    public final StringPath title;
    public final NumberPath<Integer> views;

    public QEvent(String variable) {
        this(Event.class, PathMetadataFactory.forVariable(variable), INITS);
    }

    public QEvent(Path<? extends Event> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvent(PathMetadata metadata, PathInits inits) {
        this(Event.class, metadata, inits);
    }

    public QEvent(Class<? extends Event> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.annotation = this.createString("annotation");
        this.confirmedRequests = this.createNumber("confirmedRequests", Integer.class);
        this.createdOn = this.createDateTime("createdOn", LocalDateTime.class);
        this.description = this.createString("description");
        this.eventDate = this.createDateTime("eventDate", LocalDateTime.class);
        this.id = this.createNumber("id", Integer.class);
        this.paid = this.createBoolean("paid");
        this.participantLimit = this.createNumber("participantLimit", Integer.class);
        this.publishedOn = this.createDateTime("publishedOn", LocalDateTime.class);
        this.requestModeration = this.createBoolean("requestModeration");
        this.state = this.createEnum("state", State.class);
        this.title = this.createString("title");
        this.views = this.createNumber("views", Integer.class);
        this.category = inits.isInitialized("category") ? new QCategory(this.forProperty("category")) : null;
        this.initiator = inits.isInitialized("initiator") ? new QUser(this.forProperty("initiator")) : null;
        this.location = inits.isInitialized("location") ? new QLocation(this.forProperty("location")) : null;
    }

    static {
        INITS = PathInits.DIRECT2;
        event = new QEvent("event");
    }
}
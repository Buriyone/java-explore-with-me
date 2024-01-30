package ru.practicum.request.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.*;
import ru.practicum.assistant.State;
import ru.practicum.event.model.QEvent;
import ru.practicum.user.model.QUser;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;

@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParticipationRequest extends EntityPathBase<ParticipationRequest> {
    private static final long serialVersionUID = 1478225225L;
    private static final PathInits INITS;
    public static final QParticipationRequest participationRequest;
    public final DateTimePath<LocalDateTime> created;
    public final QEvent event;
    public final NumberPath<Integer> id;
    public final QUser requester;
    public final EnumPath<State> status;

    public QParticipationRequest(String variable) {
        this(ParticipationRequest.class, PathMetadataFactory.forVariable(variable), INITS);
    }

    public QParticipationRequest(Path<? extends ParticipationRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QParticipationRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QParticipationRequest(PathMetadata metadata, PathInits inits) {
        this(ParticipationRequest.class, metadata, inits);
    }

    public QParticipationRequest(Class<? extends ParticipationRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.created = this.createDateTime("created", LocalDateTime.class);
        this.id = this.createNumber("id", Integer.class);
        this.status = this.createEnum("status", State.class);
        this.event = inits.isInitialized("event") ? new QEvent(this.forProperty("event"), inits.get("event")) : null;
        this.requester = inits.isInitialized("requester") ? new QUser(this.forProperty("requester")) : null;
    }

    static {
        INITS = PathInits.DIRECT2;
        participationRequest = new QParticipationRequest("participationRequest");
    }
}
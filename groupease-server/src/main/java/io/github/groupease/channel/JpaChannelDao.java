package io.github.groupease.channel;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * JPA implementation of {@link ChannelDao}.
 */
public class JpaChannelDao implements ChannelDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final EntityManager entityManager;

    /**
     * Injectable constructor.
     *
     * @param entityManager
     */
    @Inject
    public JpaChannelDao(
            @Nonnull EntityManager entityManager
    ) {
        this.entityManager = requireNonNull(entityManager);
    }

    @Nonnull
    @Override
    public List<Channel> list() {
        LOGGER.debug("JpaChannelDao.list() called.");

        TypedQuery<ChannelDto> query = entityManager.createQuery(
                "SELECT dto FROM ChannelDto dto ORDER BY dto.name ASC",
                ChannelDto.class
        );
        List<ChannelDto> channelDtoList = query.getResultList();

        List<Channel> channels = new ArrayList<>();

        for (ChannelDto channelDto : channelDtoList) {
            channels.add(
                    Channel.Builder.from(channelDto)
                            .build()
            );
        }

        return channels;
    }

    @Nonnull
    @Override
    public Channel getById(
            long id
    ) {
        LOGGER.debug("JpaChannelDao.getById({}) called.", id);

        ChannelDto channelDto = entityManager.find(
                ChannelDto.class,
                id
        );

        return Channel.Builder.from(channelDto)
                .build();
    }

    @Nonnull
    @Override
    public Channel update(
            @Nonnull ChannelDto toUpdate
    ) {
        LOGGER.debug("JpaChannelDao.update({}) called.", toUpdate);

        ChannelDto channelDto = entityManager.merge(toUpdate);

        return Channel.Builder.from(channelDto)
                .build();
    }

    @Nonnull
    @Override
    public Channel create(
            @Nonnull ChannelDto toCreate
    ) {
        LOGGER.debug("JpaChannelDao.create({}) called.", toCreate);

        entityManager.persist(toCreate);

        return Channel.Builder.from(toCreate)
                .build();
    }

    @Override
    public void delete(long id) {
        LOGGER.debug("JpaChannelDao.delete({}) called.", id);

        ChannelDto channelDto = entityManager.find(
                ChannelDto.class,
                id
        );
        entityManager.remove(channelDto);
    }

}

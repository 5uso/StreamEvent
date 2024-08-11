package suso.event_common.custom.entities;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.*;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;

public class EntityTypeSettings {
    private final SpawnGroup spawnGroup;
    private ImmutableSet<Block> canSpawnInside = ImmutableSet.of();
    private boolean saveable = true;
    private boolean summonable = true;
    private boolean fireImmune;
    private boolean spawnableFarFromPlayer;
    private int maxTrackingRange = 5;
    private int trackingTickInterval = 3;
    private EntityDimensions dimensions = EntityDimensions.changing(0.6F, 1.8F);
    private float spawnBoxScale = 1.0F;
    private EntityAttachments.Builder attachments = EntityAttachments.builder();
    private FeatureSet requiredFeatures;

    private EntityTypeSettings(SpawnGroup spawnGroup) {
        this.requiredFeatures = FeatureFlags.VANILLA_FEATURES;
        this.spawnGroup = spawnGroup;
        this.spawnableFarFromPlayer = spawnGroup == SpawnGroup.CREATURE || spawnGroup == SpawnGroup.MISC;
    }

    public static EntityTypeSettings create(SpawnGroup spawnGroup) {
        return new EntityTypeSettings(spawnGroup);
    }

    public EntityTypeSettings dimensions(float width, float height) {
        this.dimensions = EntityDimensions.changing(width, height);
        return this;
    }

    public EntityTypeSettings spawnBoxScale(float spawnBoxScale) {
        this.spawnBoxScale = spawnBoxScale;
        return this;
    }

    public EntityTypeSettings eyeHeight(float eyeHeight) {
        this.dimensions = this.dimensions.withEyeHeight(eyeHeight);
        return this;
    }

    public EntityTypeSettings passengerAttachments(float... offsetYs) {
        float[] var2 = offsetYs;
        int var3 = offsetYs.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            float f = var2[var4];
            this.attachments = this.attachments.add(EntityAttachmentType.PASSENGER, 0.0F, f, 0.0F);
        }

        return this;
    }

    public EntityTypeSettings passengerAttachments(Vec3d... passengerAttachments) {
        Vec3d[] var2 = passengerAttachments;
        int var3 = passengerAttachments.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Vec3d vec3d = var2[var4];
            this.attachments = this.attachments.add(EntityAttachmentType.PASSENGER, vec3d);
        }

        return this;
    }

    public EntityTypeSettings vehicleAttachment(Vec3d vehicleAttachment) {
        return this.attachment(EntityAttachmentType.VEHICLE, vehicleAttachment);
    }

    public EntityTypeSettings vehicleAttachment(float offsetY) {
        return this.attachment(EntityAttachmentType.VEHICLE, 0.0F, -offsetY, 0.0F);
    }

    public EntityTypeSettings nameTagAttachment(float offsetY) {
        return this.attachment(EntityAttachmentType.NAME_TAG, 0.0F, offsetY, 0.0F);
    }

    public EntityTypeSettings attachment(EntityAttachmentType type, float offsetX, float offsetY, float offsetZ) {
        this.attachments = this.attachments.add(type, offsetX, offsetY, offsetZ);
        return this;
    }

    public EntityTypeSettings attachment(EntityAttachmentType type, Vec3d offset) {
        this.attachments = this.attachments.add(type, offset);
        return this;
    }

    public EntityTypeSettings disableSummon() {
        this.summonable = false;
        return this;
    }

    public EntityTypeSettings disableSaving() {
        this.saveable = false;
        return this;
    }

    public EntityTypeSettings makeFireImmune() {
        this.fireImmune = true;
        return this;
    }

    public EntityTypeSettings allowSpawningInside(Block... blocks) {
        this.canSpawnInside = ImmutableSet.copyOf(blocks);
        return this;
    }

    public EntityTypeSettings spawnableFarFromPlayer() {
        this.spawnableFarFromPlayer = true;
        return this;
    }

    public EntityTypeSettings maxTrackingRange(int maxTrackingRange) {
        this.maxTrackingRange = maxTrackingRange;
        return this;
    }

    public EntityTypeSettings trackingTickInterval(int trackingTickInterval) {
        this.trackingTickInterval = trackingTickInterval;
        return this;
    }

    public EntityTypeSettings requires(FeatureFlag... features) {
        this.requiredFeatures = FeatureFlags.FEATURE_MANAGER.featureSetOf(features);
        return this;
    }

    public <T extends Entity> EntityType<T> build(EntityType.EntityFactory<T> factory) {
        if (this.saveable) Util.getChoiceType(TypeReferences.ENTITY_TREE, null);
        return new EntityType<>(factory, this.spawnGroup, this.saveable, this.summonable, this.fireImmune, this.spawnableFarFromPlayer, this.canSpawnInside, this.dimensions.withAttachments(this.attachments), this.spawnBoxScale, this.maxTrackingRange, this.trackingTickInterval, this.requiredFeatures);
    }
}

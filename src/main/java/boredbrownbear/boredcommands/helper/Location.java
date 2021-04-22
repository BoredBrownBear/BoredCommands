package boredbrownbear.boredcommands.helper;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * contain a chunk location x,y,z
 * and a exact position posX,posY,posZ
 * <p>
 * z and posZ represents height.
 */
public class Location {
    public double x, y, z;
    public double posX, posY, posZ;
    public float yaw, pitch;

    public Location(BlockPos pos) {
        init(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
    }

    public Location(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;

        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    /**
     * construct a location from where the player is looking at.
     */
    public Location(PlayerEntity player) {
        init(player.getX(), player.getY(), player.getZ(), player.yaw, player.pitch);
    }

    /**
     * construct a location from another location's toString().
     */
    public Location(String info) {
        String[] part = info.split("[,]");
        try {
            init(Double.parseDouble(part[0]), Double.parseDouble(part[1]), Double.parseDouble(part[2]), Float.parseFloat(part[3]), Float.parseFloat(part[4]));
        } catch (Exception e) {
            System.err.println("Exception on attempt to rebuild Location from String.");
            init(0, 0, 0, 0, 0);
        }
    }

    private void init(PlayerEntity player) {
        this.x = round(player.getX());
        this.y = round(player.getY());
        this.z = round(player.getZ());

        this.posX = player.getX();
        this.posY = player.getY();
        this.posZ = player.getZ();

        this.yaw = player.yaw;
        this.pitch = player.pitch;
    }

    private void init(double posX, double posY, double posZ, float yaw, float pitch) {
        this.x = round(posX);
        this.y = round(posY);
        this.z = round(posZ);

        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;

        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * floor then cast to int.
     */
    private static int round(double pos) {
        return (int) Math.floor(pos);
    }

    public String toString() {
        return posX + "," + posY + "," + posZ + "," + yaw + "," + pitch;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Location) {
            Location location = (Location) o;
            boolean equal = true;
            equal = equal && this.posX == location.posX;
            equal = equal && this.posY == location.posY;
            equal = equal && this.posZ == location.posZ;
            equal = equal && this.yaw == location.yaw;
            equal = equal && this.pitch == location.pitch;
            return equal;
        }
        return false;
    }

    public BlockPos getPosfrom() {
        BlockPos pos1 = new BlockPos(this.x, this.y, this.z);
        return pos1;
    }
}

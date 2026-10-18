public interface Constants {
   int fontHeight = (new FontRenderer(0)).font.getHeight();
   int lineHeight = fontHeight;
   int[] armorDiamondThresholds = new int[]{150, 400, 1000, 3000};
   int[] worldUnlockRedDiamonds = new int[]{0, 10, 25};
   int[] endingCrystalOffsets = new int[]{-24, -23, 24, -23, 0, 23, 24, 23};
   int[] sealLocationOffsets = new int[]{-33, -58, 14, -58, -8, -12, 22, 2};
   byte[][] sealNavigation = new byte[][]{{-1, -1, 0, -1}, {1, -1, 3, -1}, {2, 2, -1, -1}, {-1, 0, -1, 2}};
   int checkpointTextY = 178 - fontHeight - 10;
   int menuRowHeight = fontHeight + 1;
   int loadingTextY = 230 - fontHeight + 6;
   int mapTitleBoxHeight = fontHeight + 6;
   int mapTitleBoxY = 51 - mapTitleBoxHeight - 2;
   int mapStatsBoxHeight = fontHeight + 4;
   int mapTitleTextY = mapTitleBoxY + mapTitleBoxHeight / 2;
}

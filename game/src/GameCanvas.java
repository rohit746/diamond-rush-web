import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;

public final class GameCanvas extends Canvas implements Runnable {
   private int crushTimer = 40;
   private int turnCooldown;
   private boolean underHeavyObject = false;
   private boolean keyReleased;
   private int armorLevel = 0;
   private int nextArmorThreshold = Constants.armorDiamondThresholds[this.armorLevel];
   private String shopMessage = "";
   private boolean playerVisible = true;
   private int sealLocation = 0;
   private int sealCursorX = 0;
   private int sealCursorY = 0;
   private int sealCursorPrevX = this.sealCursorX;
   private int sealCursorPrevY = this.sealCursorY;
   private String sealCaption = "";
   private String sealCaptionTarget = "";
   private int sealInputDirection = -1;
   private boolean sealCursorMoving = false;
   private boolean sealDirty = true;
   private byte sealCursorStep = 0;
   private int sealCursorTargetX = 0;
   private int sealCursorTargetY = 0;
   private int cursorAnimLength = 0;
   private int cursorAnimFrame = 0;
   private int sealX = 0;
   private int sealY = 0;
   private int crystalsPlaced = 0;
   private static boolean[] crystalInserted = new boolean[]{true, false, false};
   private int crystalInsertTicks = 0;
   private static boolean[] worldCrystalEarned = new boolean[]{false, false, false};
   private boolean crystalExitPending = false;
   private int flyingCrystal = -1;
   private int flyingCrystalX = 10;
   private int flyingCrystalY = 10;
   private int crystalHalfWidth = 0;
   private int crystalHalfHeight = 0;
   private int sealFlashStep = 0;
   private int crystalAnimLength = 0;
   private int crystalAnimFrame = 0;
   private boolean endingReached = false;
   private int bossSlamTicks = 0;
   private int bossHeldObjectX = -1;
   private int bossHeldObjectY = -1;
   private int arenaWallRow = 0;
   private int arenaWallX1 = 0;
   private int arenaWallX2 = 0;
   private int arenaWallRow2 = 0;
   private int bossPreviousAnim = 0;
   private boolean bossSlamHitPlayer = false;
   private boolean bossSlamVisible = false;
   private int unusedBossValue = 0;
   private int unlockedWorldNotice = 0;
   private int unlockedArmorNotice = 0;
   private int endingPhase = 0;
   private int endingFlashColor = 0;
   private int endingShakeX = 0;
   private int endingShakeY = 0;
   private byte endingShakeDirY = 1;
   private byte endingShakeDirX = 1;
   private byte endingShakeLimitY = 3;
   private byte endingShakeLimitX = 3;
   private byte endingShakeSpeed = 2;
   private byte unusedByteI = 0;
   private boolean hasEnemies = false;
   private int padlockGemsRemaining;
   private int padlockX;
   private int padlockY;
   private static byte[] effectPositions = new byte[16];
   private static byte[] effectTimers = new byte[8];
   private static byte[] effectTypes = new byte[8];
   private int drawnEffect;
   private int nextEffect;
   private byte checkpointHasPotion;
   private static boolean vibrationEnabled;
   private int checkpointPadlockGems;
   private int tipIndex = 0;
   private static int keysDown;
   private boolean onSolidGround = true;
   private boolean hudDirty;
   private int takenTreasureCount;
   private int checkpointTreasureCount;
   private static byte[] takenTreasures;
   private byte stageKind = 0;
   private int checkpointBannerUntil = 0;
   private int floodHeight;
   private int floodSwitchPresses;
   private int compassDirection;
   private boolean hasCompass;
   private int bossAnim;
   private int bossTimer;
   private int bossHealth;
   private int bossPosition;
   private int yetiX;
   private int bossX;
   private boolean prologueSaw13 = false;
   private boolean prologueSaw16 = false;
   private boolean prologueMarkerReached = false;
   private boolean prologueMarkerHint = false;
   private int shakeX;
   private int shakeY;
   private static byte[] collapsingRows;
   private int collapsingRowCount;
   private int idleAnimDelay;
   private int entryWalkSteps;
   public byte playerAction = 0;
   private byte queuedAction = 0;
   private int lives;
   private int world;
   private int stage;
   public int cameraX;
   public int cameraY;
   public int cameraTargetX;
   public int cameraTargetY;
   public int mapWidth;
   public int mapHeight;
   private int nextCompassTarget;
   private int hintIconSet;
   private int hintIconFrame;
   private int pickupIconSet;
   private int pickupIconFrame;
   private int pickupValue;
   private int pickupObject;
   private int paintImageSet;
   private int paintFrame;
   private int paintOverlaySet;
   private int paintOverlayFrame;
   private int trapLengthA;
   private int trapReachA;
   private int trapLengthB;
   private int trapReachB;
   public static byte gameState = 0;
   private int subState;
   private static byte stateBeforeCheats = 0;
   private boolean resultsSkipped = false;
   private static int stateTicks;
   public static int stateFrames;
   private long stateStartTime;
   private boolean frameSkipped;
   public int playerX;
   public int playerY;
   public int moveProgress;
   public int playerFlags;
   private int escapePressesLeft;
   private boolean checkpointReached;
   private int goldKeys;
   private int silverKeys;
   private boolean cheatSkipWalking;
   private byte energy;
   private int deathTimer;
   private long invulnerableTicks;
   private int moveSpeed;
   private int stageDiamondTotal = 0;
   private int stageDiamonds;
   private int stageRedDiamondTotal = 0;
   private int stageRedDiamonds;
   private int stageHits;
   private int stageRetries;
   private int hookedObjectState;
   private int hookRestoreState;
   private byte hookPullDelay;
   private int hookX;
   private int hookY;
   private int hookPullSteps;
   private int screenShakeTicks;
   private int screenShakeOffset;
   private int fadeHeight;
   private int pendingHint = -1;
   private boolean stuckWarning;
   private int stageTitleUntil;
   private boolean menuItemChosen;
   private int menu;
   private int previousMenu;
   private int menuSelection;
   private int loadingStepCount;
   private int loadingStep;
   private int genPackNumber;
   private int rollerVariants;
   private int playerScreenX;
   private int playerScreenY;
   private int drawX;
   private int drawY;
   private int overlayDx;
   private int overlayDy;
   private int overlayTileX;
   private int overlayTileY;
   private boolean overlayPending;
   private int overlayModule = 0;
   private int scanX;
   private int scanY;
   private int paintTileX;
   private int paintTileY;
   private int paintObject;
   private int paintTerrain;
   private int paintSpriteAnim;
   private int paintSpriteFrame;
   private int unusedPaintValue;
   private int paintOffsetX;
   private int paintOffsetY;
   private int paintTransform;
   private int paintPlayerDirection;
   private int paintAnimPhase;
   private int checkpointX;
   private int checkpointY;
   private int checkpointGoldKeys;
   private int checkpointSilverKeys;
   private int checkpointEntryWalkSteps = 0;
   private int checkpointDiamonds = 0;
   private int checkpointRedDiamonds = 0;
   private int checkpointNextCompassTarget = 0;
   private int checkpointFloodSwitchPresses = 0;
   private int checkpointFloodHeight = 0;
   private boolean quitRequested;
   private int genPackEntryCount;
   private int genPackSkipBytes;
   private InputStream stagePackStream;
   private byte exitDirectionA;
   private byte exitDirectionB;
   private boolean cheatSkipExitB = false;
   private int togglePhase;
   private int toggleDirection;
   private int checkpointTogglePhase;
   private int checkpointToggleDirection;
   private Graphics screen;
   private static Image splashTop;
   private static Image splashMiddle;
   private static Image splashBottom;
   private static Image[][] imageSets;
   public static Image[][] demoUiImages;
   public static Sprite[] sprites;
   private static FontRenderer fontMedium;
   public static FontRenderer fontSmall;
   private static Sprite tipSprite;
   private static byte[] mapFileData;
   private static SpriteInstance[] spriteInstances;
   private static Sprite paintSprite;
   private static byte[] directionOffsets = new byte[]{0, 0, -1, 0, 1, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0};
   private static byte[] oppositeDirection = new byte[]{0, 3, 4, 1, 2, 5, 6};
   private long neededGenSprites;
   private long neededExtraSprites;
   private boolean creatingNewGame = false;
   private static byte[] saveData = null;
   private static byte[] preferences = null;
   public static int[][] tileMap;
   public static int[][] objectState;
   private static byte[][] objectMap;
   private static byte[][] objectTimers;
   private static byte[][] tileRedrawTimers;
   private static byte[] compassTargets;
   private boolean cameraPanToCheckpoint;
   private int cameraPanX;
   private int cameraPanY;
   private int cameraPanSpeed;
   private int switchRevealPhase;
   private static byte[] switchTargetObjects;
   private static byte[] switchStates;
   private static int activeSwitchId;
   private static int checkpointActiveSwitchId;
   private static byte[] genPackDirectory;
   private static int[][] checkpointObjectState;
   private static int[][] checkpointTileMap;
   private static byte[][] checkpointObjectTimers;
   private static byte[][] checkpointObjectMap;
   private static byte[] checkpointSwitchStates;
   public final SoundManager sound;
   private Cutscene activeCutscene = null;
   private static Cutscene[] cutscenes = null;
   private static byte[] cutsceneIds = null;
   private Thread gameThread;
   public static GloftDIRU midlet;
   private int deathX = -1;
   private int deathY = -1;
   private int deferredY;
   private int deferredX;
   private int deferredFrame;
   private static Sprite deferredSprite;
   private int deferredAnim;
   private byte checkpointBannerCount = 0;
   public static boolean pickupAnimStarting = false;
   private static String moreGamesUrl = "more_games_url";
   private long frameStartTime;
   private long lastFrameMillis;
   private boolean titleShownOnce = false;
   private boolean mapSpritesLoaded = false;
   private boolean unloadMapOnLoad = false;
   private boolean saveResultOnLoad = false;
   private boolean freeStageOnLoad = false;
   private boolean unloadCutscenesOnLoad = false;
   private boolean gcOnLoad = false;
   private boolean reloadLivesOnLoad = false;
   private boolean writeSaveOnLoad = false;
   private int armorPalette = -1;
   private int preferencesLength;
   private int saveDataLength;
   private boolean noSaveFound;
   private boolean stageCompletedBefore = false;
   private int unlockedNodeX;
   private int unlockedNodeY;
   private boolean blackScreenPending = false;
   private boolean tipVisible = false;
   private int cheatMaxEnergy;
   private int cheatWeapons;
   private boolean cheatUnlockAllStages;
   private boolean cheatUnlockSecretStages;
   private int slotFrameCycle;
   private static Image backgroundBuffer = null;
   private static Graphics backgroundBufferGraphics = null;
   private static int backgroundBufferWidth = 0;
   private static int backgroundBufferHeight = 0;
   private static int bufferedLeft = -1;
   private static int bufferedRight = -1;
   private static int bufferedTop = -1;
   private static int bufferedBottom = -1;
   private int cameraTileX;
   private int cameraTileY;
   private int cameraTileOffsetX;
   private int cameraTileOffsetY;
   private static Image unusedImage = null;
   private static int unusedIntCM = 0;
   private static int unusedIntCN = 0;
   private static int unusedIntCO = 0;
   private int hudEnergy = -1;
   private int hudDiamonds = -1;
   private int hudRedDiamonds = -1;
   private int hudLives = -1;
   private int hudPadlockGems = -1;
   private int hudGoldKeys = -1;
   private int hudSilverKeys = -1;
   private boolean hudPanelVisible = true;
   private int menuTicks;
   private int menuItemCount;
   private int menuWidth;
   private int menuItemsRevealed;
   private int menuRevealDelay = 3;
   private int menuAnimFrame = -1;
   private long menuAnimTime = 0L;
   private int menuHighlightedRow = -1;
   private static boolean menuNeedsRedraw = false;
   private static boolean pauseBackgroundStale = true;
   private long unusedTimeH = 0L;
   private boolean menuGameStarted;
   private boolean menuGameStartedStale;
   private boolean arenaWallsRestored;
   private boolean shopTextChanged;
   private boolean cheatSkipStages = false;
   private int debugEditorRow = 0;
   private int[] debugEditorValues = new int[]{0, 0, 0, 0, 0};
   private static byte cheatCodeProgress = 0;
   private static int[] cheatCodeKeys = new int[]{512, 16384, 131072, 131072, 4096};
   private static int newDiamondTotal = 0;
   private static int newRedDiamondTotal = 0;
   private static int newSaveFlags = 0;
   private byte stageAwardsBefore;
   private byte stageAwards;
   private boolean stageJustUnlocked = false;
   private static byte[] doorX = new byte[16];
   private static byte[] doorY = new byte[16];
   private long lastPickupTime = 0L;
   private boolean knightCharging;
   private int knightChargeOffset = 0;
   private boolean knightTurning;
   private long yetiNextRoar = 0L;
   private long icicleDropTime = 0L;
   private boolean paused = false;
   private long pausedAt;
   private boolean stageHasDoors9;
   private boolean stageHasDoors8;
   private boolean stageHasWindPods;
   private boolean bossExitPending = false;
   private String hintText;
   private int hintTextTicks = 0;
   private int switchTargetX;
   private int switchTargetY;
   private int switchRevealTimer;
   private static boolean cameraHeldAfterReveal = false;
   private static int actorAX;
   private static int actorAY;
   private static int actorBX;
   private static int actorBY;
   private boolean enemyRemovalPending = true;
   private int enemyChaseTicks = -1;
   private int enemyChaseSteps;
   private int enemyUnusedS;
   private byte enemyChaseDirection;
   private int enemyRemovalIndex;
   private int enemyCount;
   private int enemyTriggerX;
   private int enemyTriggerY;
   private int enemyTriggerSide;
   private byte enemyActorSlot;
   private int enemyTargetRow;
   private int enemyStepTimer = -1;
   private byte enemyPhase = 3;
   private byte checkpointEnemyPhase;
   private int checkpointEnemyChaseTicks;
   private int checkpointEnemyChaseSteps;
   private int checkpointEnemyUnusedS;
   private byte checkpointEnemyChaseDirection;
   private int checkpointEnemyTriggerX;
   private int checkpointEnemyTriggerY;
   private int checkpointEnemyTriggerSide;
   private byte checkpointEnemyActorSlot;
   private int checkpointEnemyTargetRow;
   private int checkpointEnemyStepTimer;
   private static Sprite slotSprite;
   private static int[][] tileSlots;
   private static long[] actors = new long[15];
   private static long[] enemies = new long[15];
   private static int[][] checkpointTileSlots;
   private static long[] checkpointActors;
   private static long[] checkpointEnemies;
   private static boolean checkpointEnemyRemovalPending;
   private int checkpointEnemyRemovalIndex;
   private int checkpointEnemyCount;
   private static String tipText;
   private static long[][] mapNodes;
   private static StringBuffer unusedBufferA;
   private static StringBuffer unusedBufferB;
   private static StringBuffer textBuffer;
   private static Image mapPreview;
   private static Graphics mapPreviewGraphics;
   private int firstSecretStage = 100;
   private int mapNodesDrawn;
   private int mapWalkX;
   private int mapWalkY;
   private int mapWalkError;
   private int mapWalkDx;
   private int mapWalkDy;
   private int mapWalkErrorStep;
   private int mapWalkErrorInc;
   private int mapWalkStepX;
   private int mapWalkStepY;
   private boolean mapWalkStarting = true;
   private boolean mapWalkFacingLeft;
   private int mapCursorX;
   private int mapCursorY;
   private int mapTargetX;
   private int mapTargetY;
   private static int unlockedStage;
   private int pathX;
   private int pathY;
   private int pathError;
   private int pathDx;
   private int pathDy;
   private int pathErrorStepX;
   private int pathErrorStepY;
   private int pathSteps;
   private boolean mapUnlockDone = true;
   private boolean mapPathDotsVisible = true;
   private boolean mapUnlockAnimDone = true;
   private int[] mapPathPoints;
   private int mapPathLength;
   private int mapPathShown;
   private int mapPathBlinkDelay = 2;
   private int mapUnlockFrame;
   private int mapWalkerBlinkDelay = 2;
   private boolean mapPathReversed;
   private boolean cheatSkipExitA = true;
   private boolean mapPreviewStale = true;
   private long unusedTimeM = 0L;
   private int shownArmorLevel = -1;
   private String shopDiamondsText;
   private StringBuffer armorThresholdText = new StringBuffer();
   private boolean fullRedraw = true;
   private boolean mapCursorMoving = true;
   private int creditsScroll;
   private int creditsFirstLine;
   private static short[][] menus = new short[][]{
      {0, 16, 1, 40, 6, 44, 3, 18, 5, 22},
      {0, 50, 1, 49, 6, 18, 3, 28, 4, 36, 5, 22},
      {0, 1, 1, 4, 2, 64, 3, 63},
      new short[0],
      new short[0],
      new short[0],
      {0, 50, 4, 36},
      {0, 24, 1, 25},
      {0, 44, 1, 22}
   };
   private static int[] worldNameStrings = new int[]{1, 4, 64};
   private static int[][] stageNumbers = new int[][]{
      {1, 2, 3, 4, 5, 6, 7, 8, 9, 12, 13, 14, 15}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}
   };
   private static String[] stageNames;
   public static String[] strings;
   public static String[] tutorialStrings;
   public static boolean rightToLeft = false;
   private static byte[] creditsText = null;
   private static String[] stagePackFiles = new String[]{"/w0.bin", "/w1.bin", "/w2.bin"};
   private static int noticeMillis = 0;
   private long inactivityClock = System.currentTimeMillis();
   private String noticeText;
   private int noticeX;
   private int noticeY;
   private int noticeColor;
   private int noticeStyle;

   public GameCanvas(GloftDIRU var1) {
      this.hintIconSet = -1;
      this.pickupIconSet = -1;
      this.pickupValue = -1;
      spriteInstances = new SpriteInstance[6];
      sprites = new Sprite[61];
      GameCanvas gamecanvas = this;
      if (Sprite.mcTableUnused == null) {
         InputStream inputstream = gamecanvas.getClass().getResourceAsStream("/mc");
         Sprite.mcTableUnused = new byte[256];

         try {
            inputstream.read(Sprite.mcTableUnused);
            inputstream.close();
         } catch (Exception exception) {
         }
      }

      imageSets = new Image[33][];
      demoUiImages = new Image[2][];
      midlet = var1;
      this.sound = new SoundManager();
      this.setFullScreenMode(true);
      this.hudDirty = true;
      vibrationEnabled = true;
      moreGamesUrl = midlet.getAppProperty(moreGamesUrl);
      this.gameThread = new Thread(this);
      this.gameThread.start();
      short[] ashort;
      byte b0;
      if (!isMoreGamesEnabled()) {
         menus[0] = new short[8];
         menus[0][0] = 0;
         menus[0][1] = 16;
         menus[0][2] = 1;
         menus[0][3] = 40;
         menus[0][4] = 3;
         menus[0][5] = 18;
         menus[0][6] = 5;
         ashort = menus[0];
         b0 = 7;
      } else {
         menus[0] = new short[10];
         menus[0][0] = 0;
         menus[0][1] = 16;
         menus[0][2] = 1;
         menus[0][3] = 40;
         menus[0][4] = 6;
         menus[0][5] = 44;
         menus[0][6] = 3;
         menus[0][7] = 18;
         menus[0][8] = 5;
         ashort = menus[0];
         b0 = 9;
      }

      ashort[b0] = 22;
   }

   private void hurtPlayer(int var1, int var2, int var3) {
      this.pickupIconSet = -1;
      this.pickupObject = -1;
      this.pickupValue = -1;
      int i = spriteInstances[(this.playerFlags & 16384) == 0 ? 0 : 3].anim;
      if (spriteInstances[(this.playerFlags & 16384) == 0 ? 0 : 3].anim != 40) {
         if (i != 48) {
            if (i != 47) {
               if (this.invulnerableTicks <= 0L && this.deathTimer == 0 && this.hookPullSteps == 0 && this.queuedAction != 6 && (this.playerFlags & 112) == 0
                  || this.escapePressesLeft > 0) {
                  this.stageHits++;
                  this.setEnergy((byte)(this.energy - var1));
                  if (this.fadeHeight == 0 && this.energy == 0) {
                     this.yetiNextRoar = 0L;
                     this.deathX = this.scanX;
                     this.deathY = this.scanY;
                     deferredSprite = null;
                  }

                  this.playerFlags = this.playerFlags & -113 | var2;
                  this.playSound(5);
                  switch (var2) {
                     case 16:
                        this.shakeX = this.shakeY = 0;
                        this.escapePressesLeft = 5;
                        objectMap[this.playerX][this.playerY] = 9;
                        this.playerFlags &= -113;
                        objectState[this.playerX][this.playerY] = 138412032;
                        tileRedrawTimers[this.playerX][this.playerY] = 24;
                        return;
                     case 64:
                        this.setPlayerAnim(1000);
                        return;
                  }

                  this.setPlayerAnim(10);
                  if (var3 != 0) {
                     byte b0 = (byte)var3;

                     do {
                        var2 = this.playerX - directionOffsets[b0];
                        i = this.playerY - directionOffsets[b0 + 8];
                        if (objectMap[var2][i] < 0 && (byte)tileMap[var2][i] < 0) {
                           this.playerX = var2;
                           this.playerY = i;
                           this.moveProgress = 18;
                           this.playerAction = 0;
                           this.playerFlags = this.playerFlags & -8 | b0 | 2048;
                           return;
                        }
                     } while ((b0 = b0 >= 4 ? 1 : (byte)(b0 + 1)) != var3);
                  }
               }
            }
         }
      }
   }

   private void setEnergy(byte var1) {
      label14: {
         this.energy = var1;
         GameCanvas gamecanvas;
         byte b0;
         if (this.energy <= 0) {
            gamecanvas = this;
            b0 = 0;
         } else {
            if (this.energy <= saveData[8]) {
               break label14;
            }

            gamecanvas = this;
            b0 = saveData[8];
         }

         gamecanvas.energy = b0;
      }

      this.hudDirty = true;
   }

   private void openMenu(int var1) {
      this.previousMenu = this.menu;
      this.menuItemChosen = false;
      this.menuSelection = 0;
      this.menu = var1;
      this.menuTicks = 0;
      if (var1 >= 0) {
         GameCanvas gamecanvas;
         (gamecanvas = this).menuItemCount = menus[gamecanvas.menu].length >> 1;
         gamecanvas.menuWidth = 0;

         for (int j = 0; j < gamecanvas.menuItemCount; j++) {
            int i = measureFirstLine(fontMedium, strings[menus[gamecanvas.menu][(j << 1) + 1]], 0);
            if ((gamecanvas.menu != 0 || j != 3) && i > gamecanvas.menuWidth) {
               gamecanvas.menuWidth = i;
            }
         }

         gamecanvas.menuItemsRevealed = 0;
         gamecanvas.menuTicks = 0;
      }
   }

   private static boolean isKeyDown(int var0) {
      return (keysDown & var0) != 0;
   }

   public final void keyPressed(int var1) {
      if (var1 != -10) {
         if ((var1 = keyBit(var1)) == 0 && gameState == 30) {
            var1 = 53;
         }

         keysDown |= var1;
         this.keyReleased = false;
      }
   }

   public final void keyReleased(int var1) {
      if (var1 != -10) {
         if (gameState == 30 && keyBit(var1) == 0) {
            var1 = 53;
         }

         keysDown = keysDown & ~keyBit(var1);
         this.keyReleased = true;
      }
   }

   private void resetStateTimer() {
      this.stateStartTime = System.currentTimeMillis();
      stateTicks = 0;
      stateFrames = 0;
      this.checkpointBannerUntil = 0;
   }

   private static void sleep(long var0) {
      if (var0 > 0L) {
         System.currentTimeMillis();

         try {
            Thread.sleep(var0);
         } catch (Exception exception) {
         }
      }
   }

   public final void run() {
      this.stateStartTime = System.currentTimeMillis();
      this.resetStateTimer();
      GameCanvas gamecanvas = this;
      RecordStore recordstore = null;

      try {
         try {
            recordstore = RecordStore.openRecordStore("Preferences", false);
         } catch (Exception exception4) {
            System.out.println("Exception " + exception4);
         }

         preferences = new byte[1];
         if (recordstore == null) {
            try {
               recordstore = RecordStore.openRecordStore("Preferences", true);
               preferences[0] = 0;
               gamecanvas.preferencesLength = preferences.length;
               recordstore.closeRecordStore();
               gamecanvas.writePreferences();
            } catch (Exception exception3) {
            }
         } else {
            try {
               preferences = recordstore.getRecord(1);
               gamecanvas.preferencesLength = preferences.length;
               recordstore.closeRecordStore();
               recordstore = null;
            } catch (Exception exception2) {
            }
         }
      } finally {
         try {
            if (recordstore != null) {
               recordstore.closeRecordStore();
            }
         } catch (Exception exception) {
         }
      }

      while (!this.quitRequested) {
         if (!this.paused) {
            this.frameStartTime = System.currentTimeMillis();

            try {
               this.update();
            } catch (Exception exception1) {
               exception1.printStackTrace();
               sleep(5000L);
            }

            if (this.quitRequested) {
               break;
            }

            if (gameState != 2) {
               stateTicks++;
            }

            System.currentTimeMillis();
            if (this.lastFrameMillis > 65L) {
               this.frameSkipped = true;
               this.lastFrameMillis = 0L;
            } else {
               this.repaint();
               this.frameSkipped = false;
               if (gameState != 2) {
                  stateFrames++;
               }

               this.lastFrameMillis = Math.abs(System.currentTimeMillis() - this.frameStartTime);
               sleep(50L - (System.currentTimeMillis() - this.frameStartTime));
            }
         }
      }

      this.freeStageResources();
      this.sound.shutdown();
      midlet.exitGame();
   }

   private void update() {
      GameCanvas gamecanvas1 = this;
      if (noticeMillis > 0) {
         noticeMillis = (int)(noticeMillis - (System.currentTimeMillis() - gamecanvas1.inactivityClock));
         gamecanvas1.inactivityClock = System.currentTimeMillis();
         if (noticeMillis <= 0) {
            gamecanvas1.dismissNotice(true);
         }
      }

      gamecanvas1 = this;
      if (keysDown == 0) {
         gamecanvas1.playerAction = 0;
      } else if (noticeMillis > 0) {
         gamecanvas1.dismissNotice(true);
         keysDown = 0;
      } else {
         label1508: {
            int k14;
            label1360: {
               switch (gameState) {
                  case 1:
                     if (gamecanvas1.crystalExitPending || gamecanvas1.bossExitPending) {
                        keysDown = 0;
                     }

                     stateBeforeCheats = gameState;
                     gamecanvas1.checkCheatCode();
                     GameCanvas gamecanvas10 = gamecanvas1;
                     if (gamecanvas1.cheatSkipStages && (isKeyDown(524288) || isKeyDown(131072))) {
                        GameCanvas gamecanvas11 = gamecanvas10;
                        if (gamecanvas10.world == 0 && gamecanvas11.stage == 13) {
                           gamecanvas11.activeCutscene = null;
                           gamecanvas11.playerX = 60;
                           gamecanvas11.playerY = 3;
                        } else {
                           gamecanvas11.cheatSkipExitA = isKeyDown(524288);
                           gamecanvas11.cheatSkipExitB = !gamecanvas11.cheatSkipExitA;
                           gamecanvas11.stageRetries = 0;
                           gamecanvas11.stageHits = 0;
                           gamecanvas11.cheatSkipWalking = true;
                           gamecanvas11.playerX = gamecanvas11.mapWidth + 5 + 1;
                        }
                     }

                     if ((gamecanvas10.switchRevealPhase == 0 || isKeyDown(32944))
                        && gamecanvas10.entryWalkSteps == 0
                        && !gamecanvas10.cheatSkipWalking
                        && gamecanvas10.energy > 0
                        && spriteInstances[0].anim != 19
                        && !gamecanvas10.cameraPanToCheckpoint) {
                        if (gamecanvas10.activeCutscene != null) {
                           if (isKeyDown(32784)) {
                              gamecanvas10.activeCutscene.updateText();
                           } else if (isKeyDown(32944)) {
                              gamecanvas10.activeCutscene.skipping = true;
                           }

                           keysDown = 0;
                           break label1508;
                        }

                        if (gamecanvas10.escapePressesLeft > 0) {
                           boolean flag2 = true;
                           if (isKeyDown(4097)) {
                              gamecanvas10.shakeY = -5;
                           } else if (isKeyDown(262146)) {
                              gamecanvas10.shakeY = 5;
                           } else if (isKeyDown(16388)) {
                              gamecanvas10.shakeX = -5;
                           } else if (isKeyDown(65544)) {
                              gamecanvas10.shakeX = 5;
                           } else if (!isKeyDown(32784)) {
                              if (isKeyDown(32944)) {
                                 gamecanvas10.openPauseMenu();
                                 keysDown = 0;
                              } else {
                                 flag2 = false;
                              }
                           }

                           if (flag2) {
                              gamecanvas10.escapePressesLeft--;
                              if (gamecanvas10.escapePressesLeft == 0) {
                                 if ((byte)tileMap[gamecanvas10.playerX][gamecanvas10.playerY] < 0) {
                                    tileMap[gamecanvas10.playerX][gamecanvas10.playerY] = 32;
                                 }

                                 if (objectMap[gamecanvas10.playerX][gamecanvas10.playerY] == 9) {
                                    objectMap[gamecanvas10.playerX][gamecanvas10.playerY] = -1;
                                 }

                                 gamecanvas10.invulnerableTicks = 40L;
                                 gamecanvas10.moveProgress = 0;
                                 gamecanvas10.playerFlags &= -113;
                                 gamecanvas10.setPlayerAnim(gamecanvas10.hitRecoveryAnim());
                              }

                              keysDown = 0;
                           }
                           break label1508;
                        }

                        if (isKeyDown(4097)) {
                           gamecanvas10.playerAction = 1;
                        } else if (isKeyDown(262146)) {
                           gamecanvas10.playerAction = 3;
                        } else if (isKeyDown(16388)) {
                           gamecanvas10.playerAction = 4;
                        } else if (isKeyDown(65544)) {
                           gamecanvas10.playerAction = 2;
                        } else if (isKeyDown(32784)) {
                           keysDown = 0;
                           if (gamecanvas10.checkpointX == gamecanvas10.playerX
                              && gamecanvas10.checkpointY == gamecanvas10.playerY
                              && (tileMap[gamecanvas10.playerX][gamecanvas10.playerY] & 0xFF) == 4) {
                              gamecanvas10.playSound(9);
                              gamecanvas10.restoreCheckpoint();
                           } else {
                              int i5;
                              if ((i5 = tileSlots == null ? 0 : slotBits(tileSlots[gamecanvas10.playerX][gamecanvas10.playerY], (byte)0, (byte)3, (byte)4))
                                    != 8
                                 && i5 != 7) {
                                 int i7 = gamecanvas10.playerFlags & 7;
                                 int i8 = -1;
                                 byte b2 = -1;
                                 boolean flag3 = false;
                                 if (saveData[9] >= 2) {
                                    int l10 = 0;

                                    for (int k8 = 0; k8 < 2; k8++) {
                                       byte b4;
                                       if ((b4 = (byte)(k8 == 0 ? 1 : -1)) > 0 && gamecanvas10.playerX < gamecanvas10.mapWidth - 3
                                          || b4 < 0 && gamecanvas10.playerX > 3) {
                                          for (int k12 = 1; k12 <= 3; k12++) {
                                             label1482: {
                                                int j13 = gamecanvas10.playerX + b4 * k12;
                                                byte b5 = objectMap[j13][gamecanvas10.playerY];
                                                if ((tileMap[j13][gamecanvas10.playerY] & 0xFF) != 7 || (tileMap[j13][gamecanvas10.playerY] >> 8 & 240) != 0) {
                                                   if (b5 == 48 && (objectState[j13][gamecanvas10.playerY] & 8) != 0) {
                                                      continue;
                                                   }

                                                   switch (b5) {
                                                      case -1:
                                                         continue;
                                                      case 11:
                                                      case 19:
                                                      case 43:
                                                         label1260: {
                                                            byte b7;
                                                            if (b4 > 0) {
                                                               k14 = l10;
                                                               b7 = 2;
                                                            } else {
                                                               if (b4 >= 0) {
                                                                  break label1260;
                                                               }

                                                               k14 = l10;
                                                               b7 = 4;
                                                            }

                                                            l10 = k14 | b7;
                                                         }
                                                      case 0:
                                                      case 1:
                                                      case 8:
                                                      case 9:
                                                      case 14:
                                                      case 47:
                                                      case 48:
                                                         if (k12 == 1) {
                                                            k14 = (byte)4;
                                                            break label1482;
                                                         }

                                                         if (b2 < 0) {
                                                            b2 = (byte)(b4 > 0 ? 2 : 4);
                                                         } else {
                                                            flag3 = true;
                                                            b2 = (byte)(l10 == 2 ? 2 : (l10 == 4 ? 4 : -1));
                                                         }
                                                   }
                                                }

                                                k14 = (byte)4;
                                             }

                                             k12 = k14;
                                          }
                                       }
                                    }
                                 }

                                 if (saveData[9] >= 1) {
                                    gamecanvas10.playerAction = 5;
                                    int i11 = 0;
                                    int[] aint = new int[]{0, 1, 0, -1, 1, 1, -1, -1, 0, 2, 0, -2};
                                    int[] aint1 = new int[]{-1, 0, 1, 0, -1, 1, 1, -1, -2, 0, 2, 0};
                                    int[] aint2 = new int[]{0, 0, 0, 0, 3, 6, 12, 9, 1, 2, 4, 8};

                                    for (int j14 = 0; j14 < aint.length; j14++) {
                                       i5 = gamecanvas10.playerX + aint[j14];
                                       int j12 = gamecanvas10.playerY + aint1[j14];
                                       if (i5 >= 0 && i5 < gamecanvas10.mapWidth && j12 >= 0 && j12 < gamecanvas10.mapHeight) {
                                          boolean flag;
                                          int l12;
                                          boolean flag4;
                                          int i14;
                                          label1227: {
                                             l12 = objectState[i5][j12] & 7;
                                             flag4 = false;
                                             i14 = -1;
                                             flag = false;
                                             switch (objectMap[i5][j12]) {
                                                case 9:
                                                case 18:
                                                case 30:
                                                   if (aint2[j14] == 0) {
                                                      flag = true;
                                                   }
                                                   break label1227;
                                                case 19:
                                                case 43:
                                                   if ((objectState[i5][j12] & 248) != 0) {
                                                      break label1227;
                                                   }
                                                   break;
                                                case 45:
                                                   if ((objectState[i5][j12] & 15) == 10) {
                                                      break label1227;
                                                   }
                                                   break;
                                                case 46:
                                                case 49:
                                                case 50:
                                                   if (aint2[j14] == 0) {
                                                      i11++;
                                                      flag = true;
                                                   }
                                                default:
                                                   break label1227;
                                             }

                                             flag4 = true;
                                          }

                                          if (flag4) {
                                             label1218: {
                                                if (aint2[j14] == 0) {
                                                   k14 = j14 + 1;
                                                } else {
                                                   if (objectTimers[i5][j12] < 12) {
                                                      break label1218;
                                                   }

                                                   if ((aint2[j14] & 1) == 0 || l12 != 3) {
                                                      if ((aint2[j14] & 8) == 0 || l12 != 2) {
                                                         break label1218;
                                                      }

                                                      if (aint1[j14] == 0) {
                                                         k14 = 4;
                                                      } else if (aint1[j14] < 0) {
                                                         k14 = 1;
                                                      } else {
                                                         if (aint1[j14] <= 0) {
                                                            break label1218;
                                                         }

                                                         k14 = 3;
                                                      }
                                                   } else if (aint[j14] == 0) {
                                                      k14 = 1;
                                                   } else if (aint[j14] < 0) {
                                                      k14 = 4;
                                                   } else {
                                                      if (aint[j14] <= 0) {
                                                         break label1218;
                                                      }

                                                      k14 = 2;
                                                   }
                                                }

                                                i14 = k14;
                                             }

                                             if (i14 != -1) {
                                                flag = true;
                                                i11++;
                                             }
                                          }

                                          if (flag) {
                                             if (i11 == 0) {
                                                if (i7 == j14 + 1) {
                                                   i8 = i7;
                                                } else if (i8 < 0) {
                                                   i8 = j14 + 1;
                                                }
                                             } else {
                                                if (i11 != 1) {
                                                   i8 = i7;
                                                   break;
                                                }

                                                i8 = i14;
                                             }
                                          }
                                       }
                                    }
                                 }

                                 label1472: {
                                    GameCanvas gamecanvas17;
                                    int j15;
                                    int i16;
                                    if (i8 > 0 && b2 == i7) {
                                       gamecanvas10.playerAction = 6;
                                       gamecanvas17 = gamecanvas10;
                                       j15 = gamecanvas10.playerFlags & -8;
                                       i16 = i7;
                                    } else if (i8 > 0 && b2 < 0 && !flag3) {
                                       gamecanvas17 = gamecanvas10;
                                       j15 = gamecanvas10.playerFlags & -8;
                                       i16 = i8;
                                    } else {
                                       label1509: {
                                          if (i8 >= 0 || b2 <= 0 || flag3) {
                                             if (flag3 && (i7 == 2 || i7 == 4)) {
                                                gamecanvas10.playerAction = 6;
                                                gamecanvas17 = gamecanvas10;
                                                j15 = gamecanvas10.playerFlags & -8;
                                                i16 = i7;
                                                break label1509;
                                             }

                                             if (!flag3 || b2 <= 0) {
                                                break label1472;
                                             }
                                          }

                                          gamecanvas10.playerAction = 6;
                                          gamecanvas17 = gamecanvas10;
                                          j15 = gamecanvas10.playerFlags & -8;
                                          i16 = b2;
                                       }
                                    }

                                    gamecanvas17.playerFlags = j15 | i16;
                                 }

                                 if (gamecanvas10.playerAction == 6
                                    && (tileMap[gamecanvas10.playerX][gamecanvas10.playerY] & 0xFF) == 2
                                    && tileMap[gamecanvas10.playerX][gamecanvas10.playerY] >> 8 == 1) {
                                    gamecanvas10.hintIconSet = -1;
                                    gamecanvas10.clearTileRegion(gamecanvas10.playerX, gamecanvas10.playerY, (byte)2);
                                 }
                              }
                           }
                        } else if (isKeyDown(256)) {
                           gamecanvas10.hintIconSet = -1;
                           int j5 = spriteInstances[0].anim;
                           if (spriteInstances[0].anim == 36 + (gamecanvas10.playerFlags & 7) - 1) {
                              if ((tileMap[gamecanvas10.playerX][gamecanvas10.playerY] & 0xFF) == 4) {
                                 gamecanvas10.restoreCheckpoint();
                              } else {
                                 gamecanvas10.playSound(2);
                                 gamecanvas10.setPlayerAnim(19);
                              }
                           }

                           switch (j5) {
                              case 0:
                              case 1:
                              case 2:
                              case 3:
                              case 34:
                              case 35:
                                 if ((tileMap[gamecanvas10.playerX][gamecanvas10.playerY] & 0xFF) == 4) {
                                    gamecanvas10.restoreCheckpoint();
                                 } else {
                                    gamecanvas10.playSound(2);
                                    gamecanvas10.setPlayerAnim(19);
                                 }
                           }
                        } else if (isKeyDown(32944)) {
                           gamecanvas10.openPauseMenu();
                           keysDown = 0;
                        }

                        if (gamecanvas10.playerAction != 5
                           && gamecanvas10.queuedAction == 0
                           && gamecanvas10.turnCooldown == 0
                           && gamecanvas10.playerAction != (gamecanvas10.playerFlags & 7)) {
                           gamecanvas10.playerFlags |= 4096;
                        }
                        break label1508;
                     }

                     keysDown = 0;
                     break label1508;
                  case 2:
                  case 7:
                  case 32:
                     gamecanvas1.updateMenu();
                  case 3:
                  case 5:
                  case 6:
                  case 8:
                  case 9:
                  case 10:
                  case 11:
                  case 13:
                  case 14:
                  case 16:
                  case 18:
                  case 19:
                  case 21:
                  case 22:
                  case 23:
                  case 28:
                  case 29:
                  default:
                     break label1508;
                  case 4:
                     gamecanvas1.updateMenu();
                     break label1508;
                  case 12:
                     GameCanvas gamecanvas9 = gamecanvas1;
                     if (isKeyDown(32944)) {
                        gamecanvas9.lives = 5;
                        saveData[3] = (byte)gamecanvas9.lives;
                        if (gamecanvas9.stageKind == 2) {
                           gamecanvas9.startStage();
                        } else {
                           addToSaveShort(4, -500);
                           gamecanvas9.writeSave();
                           gameState = 15;
                           gamecanvas9.freeStageOnLoad = true;
                           gamecanvas9.unloadMapOnLoad = true;
                           gamecanvas9.openWorldMap();
                        }
                     }
                     break label1508;
                  case 15:
                     GameCanvas gamecanvas8 = gamecanvas1;
                     if (gamecanvas1.mapCursorX != gamecanvas8.mapTargetX || gamecanvas8.mapCursorY != gamecanvas8.mapTargetY) {
                        break label1508;
                     }

                     byte b1 = -1;
                     if (isKeyDown(4097)) {
                        b1 = 2;
                     } else if (isKeyDown(262146)) {
                        b1 = 3;
                     } else if (isKeyDown(16388)) {
                        b1 = 4;
                     } else if (isKeyDown(65544)) {
                        b1 = 1;
                     } else {
                        if (isKeyDown(32944)) {
                           if (System.currentTimeMillis() >= 2000L) {
                              int l9 = longBits(mapNodes[gamecanvas8.mapCursorX][gamecanvas8.mapCursorY], (byte)6, (byte)5);
                              gamecanvas8.freeStageResources();
                              gamecanvas8.unloadMapResources();
                              System.gc();
                              gamecanvas8.stage = l9;
                              gamecanvas8.startStage();
                              keysDown = 0;
                           }
                           break label1508;
                        }

                        if (isKeyDown(64)) {
                           gamecanvas8.freeStageOnLoad = true;
                           gamecanvas8.unloadMapOnLoad = true;
                           gamecanvas8.gcOnLoad = true;
                           gamecanvas8.loadingStep = 0;
                           gameState = 28;
                           keysDown = 0;
                           break label1508;
                        }
                     }

                     keysDown = 0;
                     if (b1 == -1) {
                        break label1508;
                     }

                     int l6 = gamecanvas8.mapCursorX;
                     int l7 = gamecanvas8.mapCursorY;
                     long k9;
                     int i1 = longBits(k9 = mapNodes[l6][l7], (byte)11, (byte)3);
                     int k = 14;
                     int j1 = -1;
                     int l1 = -1;
                     int i2 = 0;

                     while (i2 < i1) {
                        int k1 = longBits(k9, (byte)k, (byte)4);
                        k += 4;
                        int j2 = longBits(k9, (byte)k, (byte)4);
                        label1046:
                        if (longBits(mapNodes[k1][j2], (byte)0, (byte)3) != 1) {
                           switch (b1) {
                              case 1:
                                 if (k1 <= l6) {
                                    break label1046;
                                 }

                                 if (j1 >= 0) {
                                    l1 = i2;
                                    break label1046;
                                 }

                                 k14 = i2;
                                 break;
                              case 2:
                                 if (j2 >= l7) {
                                    break label1046;
                                 }

                                 if (j1 >= 0) {
                                    l1 = i2;
                                    break label1046;
                                 }

                                 k14 = i2;
                                 break;
                              case 3:
                                 if (j2 <= l7) {
                                    break label1046;
                                 }

                                 if (j1 >= 0) {
                                    l1 = i2;
                                    break label1046;
                                 }

                                 k14 = i2;
                                 break;
                              case 4:
                                 if (k1 < l6) {
                                    if (j1 < 0) {
                                       k14 = i2;
                                       break;
                                    } else {
                                       l1 = i2;
                                    }
                                 }
                              default:
                                 break label1046;
                           }

                           j1 = k14;
                        }

                        i2++;
                        k += 4;
                     }

                     if (j1 == -1) {
                        break label1508;
                     }

                     i2 = -1;
                     label1149:
                     if (l1 != -1) {
                        label1147: {
                           k = 14 + (j1 << 1 << 2);
                           int l11 = longBits(k9, (byte)k, (byte)4);
                           k += 4;
                           int k13 = longBits(k9, (byte)k, (byte)4);
                           k = 14 + (l1 << 1 << 2);
                           int k2 = longBits(k9, (byte)k, (byte)4);
                           k += 4;
                           int l2 = longBits(k9, (byte)k, (byte)4);
                           switch (b1) {
                              case 1:
                              case 4:
                                 if (l7 == k13) {
                                    k14 = j1;
                                    break label1147;
                                 }

                                 if (l7 != l2) {
                                    k14 = Math.abs(l6 - l11) > Math.abs(l6 - k2) ? j1 : l1;
                                    break label1147;
                                 }
                                 break;
                              case 2:
                              case 3:
                                 if (l6 == l11) {
                                    k14 = j1;
                                    break label1147;
                                 }

                                 if (l6 != k2 && l11 > k2) {
                                    k14 = j1;
                                    break label1147;
                                 }
                                 break;
                              default:
                                 break label1149;
                           }

                           k14 = l1;
                        }

                        i2 = k14;
                     } else {
                        i2 = j1;
                     }

                     if (i2 != -1) {
                        k = 14 + (i2 << 1 << 2);
                        int i12 = longBits(k9, (byte)k, (byte)4);
                        k += 4;
                        int l13 = longBits(k9, (byte)k, (byte)4);
                        if (longBits(mapNodes[i12][l13], (byte)0, (byte)3) == 0) {
                           gamecanvas8.mapTargetX = i12;
                           gamecanvas8.mapTargetY = l13;
                           gamecanvas8.mapCursorMoving = true;
                        }
                     }
                     break label1508;
                  case 17:
                  case 20:
                     if (!isKeyDown(32944)) {
                        break label1508;
                     }

                     if (gamecanvas1.subState == 5) {
                        GameCanvas gamecanvas7 = gamecanvas1;
                        gamecanvas1.openWorldMap();
                        gamecanvas7.saveProgress(false);
                        gamecanvas1.freeStageResources();
                     }

                     gamecanvas1.resultsSkipped = true;
                     break;
                  case 24:
                     GameCanvas gamecanvas6 = gamecanvas1;
                     boolean flag1 = false;
                     if (isKeyDown(512)) {
                        gamecanvas6.cheatSkipStages = !gamecanvas6.cheatSkipStages;
                     } else if (isKeyDown(65536)) {
                        gamecanvas6.stageDiamonds += 50;
                     } else if (isKeyDown(131072)) {
                        gamecanvas6.stageRedDiamonds += 5;
                     } else if (isKeyDown(262144)) {
                        if (stateBeforeCheats != 1) {
                           label1093: {
                              gamecanvas6.cheatWeapons <<= 1;
                              GameCanvas gamecanvas16;
                              byte b6;
                              if (gamecanvas6.cheatWeapons == 0) {
                                 gamecanvas16 = gamecanvas6;
                                 b6 = 1;
                              } else {
                                 if (gamecanvas6.cheatWeapons <= 8) {
                                    break label1093;
                                 }

                                 gamecanvas16 = gamecanvas6;
                                 b6 = 0;
                              }

                              gamecanvas16.cheatWeapons = b6;
                           }

                           if (gamecanvas6.cheatWeapons > 2) {
                              saveData[10] = 1;
                           } else {
                              saveData[10] = 0;
                           }
                        }
                     } else if (isKeyDown(524288)) {
                        gamecanvas6.cheatUnlockAllStages = !gamecanvas6.cheatUnlockAllStages;
                     } else if (isKeyDown(1024)) {
                        gamecanvas6.cheatUnlockSecretStages = !gamecanvas6.cheatUnlockSecretStages;
                     } else {
                        label1444: {
                           if (!isKeyDown(64)) {
                              if (!isKeyDown(32944)) {
                                 break label1444;
                              }

                              saveData[8] = (byte)gamecanvas6.cheatMaxEnergy;
                              saveData[9] = (byte)gamecanvas6.cheatWeapons;
                              if (gamecanvas6.cheatUnlockAllStages || gamecanvas6.cheatUnlockSecretStages) {
                                 for (int j6 = 0; j6 <= 2; j6++) {
                                    int j = getMainStageCount(j6);
                                    if (j6 == 2) {
                                       j++;
                                    }

                                    for (int l = 0; l <= j; l++) {
                                       gamecanvas6.addStageFlags(j6, l, (byte)2);
                                       gamecanvas6.addStageFlags(j6, l, (byte)64);
                                    }

                                    raiseWorldProgress(j6, j);
                                 }

                                 if (gamecanvas6.cheatUnlockSecretStages) {
                                    for (int k6 = 0; k6 <= 2; k6++) {
                                       int k7 = getStageCount(k6);
                                       if (k6 == 0) {
                                          k7--;
                                       }

                                       for (int j9 = getMainStageCount(k6); j9 < k7; j9++) {
                                          gamecanvas6.addStageFlags(k6, j9, (byte)2);
                                          gamecanvas6.addStageFlags(k6, j9, (byte)64);
                                       }

                                       raiseWorldProgress(k6, k7 - 1);
                                    }
                                 }
                              }

                              gamecanvas6.bankStageDiamonds();
                              GameCanvas gamecanvas2 = gamecanvas6;
                              addToSaveShort(6, gamecanvas2.stageRedDiamonds);
                              gamecanvas6.writeSave();
                           }

                           flag1 = true;
                           gamecanvas6.screen.setColor(0);
                           gamecanvas6.screen.fillRect(0, 0, 320, 240);
                        }
                     }

                     if (flag1) {
                        gamecanvas6.closeCheatMenu();
                     }

                     k14 = 0;
                     break label1360;
                  case 25:
                     gamecanvas1.updateShop();
                     break label1508;
                  case 26:
                     GameCanvas gamecanvas5 = gamecanvas1;
                     if (isKeyDown(64)) {
                        gameState = 4;
                        gamecanvas5.openMenu(4);
                     }

                     k14 = 0;
                     break label1360;
                  case 27:
                     stateBeforeCheats = gameState;
                     gamecanvas1.checkCheatCode();
                     GameCanvas gamecanvas4 = gamecanvas1;
                     if (gamecanvas1.flyingCrystal == -1 && gamecanvas4.crystalsPlaced == 0) {
                        label1078: {
                           GameCanvas gamecanvas15;
                           byte b14;
                           if (isKeyDown(32944)) {
                              gamecanvas15 = gamecanvas4;
                              b14 = 4;
                           } else {
                              if (isKeyDown(64)) {
                                 gamecanvas4.freeStageResources();
                                 gameState = 9;
                                 gamecanvas4.loadingStepCount = 8;
                                 gamecanvas4.openMenu(-1);
                                 gamecanvas4.loadingStep = 0;
                                 break label1078;
                              }

                              if (isKeyDown(4097)) {
                                 gamecanvas15 = gamecanvas4;
                                 b14 = 0;
                              } else if (isKeyDown(262146)) {
                                 gamecanvas15 = gamecanvas4;
                                 b14 = 2;
                              } else if (isKeyDown(16388)) {
                                 gamecanvas15 = gamecanvas4;
                                 b14 = 3;
                              } else {
                                 if (!isKeyDown(65544)) {
                                    break label1078;
                                 }

                                 gamecanvas15 = gamecanvas4;
                                 b14 = 1;
                              }
                           }

                           gamecanvas15.sealInputDirection = b14;
                        }

                        keysDown = 0;
                        break label1508;
                     }

                     keysDown = 0;
                     break label1508;
                  case 30:
                     if (!isKeyDown(1048575)) {
                        break label1508;
                     }

                     gameState = 4;
                     if (gamecanvas1.menu == -1) {
                        gamecanvas1.subState = 0;
                        gamecanvas1.openMenu(0);
                     } else {
                        gamecanvas1.subState = 2;
                     }

                     k14 = 0;
                     break label1360;
                  case 31:
                     if (isKeyDown(64)) {
                        gamecanvas1.loadingStep = 0;
                        gamecanvas1.loadingStepCount = 8;
                        gameState = 9;
                        gamecanvas1.openMenu(-1);
                     } else if (isKeyDown(32944)) {
                        gamecanvas1.startNewGame();
                     }
                     break;
                  case 33:
                     GameCanvas gamecanvas3 = gamecanvas1;
                     if (isKeyDown(64)) {
                        if (gamecanvas3.menu == 0) {
                           gameState = 4;
                           gamecanvas3.openMenu(0);
                        }

                        if (gamecanvas3.menu == 1) {
                           gameState = 2;
                           menuNeedsRedraw = true;
                           gamecanvas3.hudDirty = true;
                           gamecanvas3.openMenu(1);
                        }
                     }
                     break label1508;
                  case 34:
                     GameCanvas gamecanvas = gamecanvas1;
                     byte b0 = 0;
                     if (isKeyDown(33008)) {
                        gamecanvas.closeCheatMenu();
                        unloadSprite(tipSprite, true);
                        tipSprite = null;
                     }

                     if (isKeyDown(4097)) {
                        gamecanvas.debugEditorRow--;
                        if (gamecanvas.debugEditorRow < 0) {
                           gamecanvas.debugEditorRow = 0;
                        }
                     }

                     if (isKeyDown(262146)) {
                        gamecanvas.debugEditorRow++;
                        if (gamecanvas.debugEditorRow >= 2) {
                           gamecanvas.debugEditorRow = 1;
                        }
                     }

                     int i = gamecanvas.debugEditorValues[gamecanvas.debugEditorRow];
                     if (isKeyDown(16388)) {
                        i--;
                     }

                     if (isKeyDown(65544)) {
                        i++;
                     }

                     if (i < 0) {
                        i = 0;
                     }

                     label1339: {
                        switch (gamecanvas.debugEditorRow) {
                           case 0:
                              k14 = (byte)115;
                              break;
                           case 1:
                              k14 = (byte)8;
                              break;
                           default:
                              break label1339;
                        }

                        b0 = (byte)k14;   // 115 or 8
                     }

                     if (i >= b0) {
                        i = b0 - 1;
                     }

                     gamecanvas.debugEditorValues[gamecanvas.debugEditorRow] = i;
               }

               k14 = 0;
            }

            keysDown = k14;
         }

         if (!gamecanvas1.keyReleased && (gamecanvas1.playerFlags & 7) != 0) {
            gamecanvas1.turnCooldown = 10;
         }
      }

      switch (gameState) {
         case 0:
            spriteInstances[0] = new SpriteInstance(loadSprite("/ui.f", 0), 0, 0, null);
            spriteInstances[0].setAnim(0);
            gameState = 6;
            this.resetStateTimer();
            return;
         case 1:
            this.updatePlaying();
            return;
         case 2:
         case 12:
            return;
         case 3:
            this.quitRequested = true;
            return;
         case 4:
         case 10:
         case 13:
         case 14:
         case 18:
         case 19:
         case 23:
         case 24:
         case 25:
         case 26:
         case 30:
         case 32:
         case 33:
         case 34:
         default:
            break;
         case 5:
            if (this.creatingNewGame && this.loadingStep <= 5) {
               this.createSaveStep(this.loadingStep++);

               for (int k4 = 0; k4 < 3; k4++) {
                  worldCrystalEarned[k4] = false;
               }

               for (int l4 = 1; l4 < 3; l4++) {
                  crystalInserted[l4] = false;
               }

               this.resetStateTimer();
               if (this.loadingStep == 5) {
                  this.writeSaveOnLoad = true;
                  this.reloadLivesOnLoad = true;
                  this.gcOnLoad = true;
                  this.world = 0;
                  this.stage = 13;
                  keysDown = 0;
               }
            } else {
               this.loadStageStep();
            }

            this.resetStateTimer();
            return;
         case 6:
            if (stateTicks < 60) {
               spriteInstances[0].updateAnim();
               return;
            }

            strings = loadStrings(127);
            tutorialStrings = loadTutorialStrings();
            stageNames = loadStageNames();

            try {
               rightToLeft = Text.get(127).trim().equals("1");
            } catch (Exception exception2) {
               exception2.printStackTrace();
               rightToLeft = false;
            }

            createFonts();
            sprites[18] = loadSprite("/ui.f", 3);
            this.freeStageResources();
            SoundManager.enabled = true;
            gameState = 8;
            this.loadingStep = 0;
            this.loadingStepCount = 32;
            return;
         case 7:
            if (this.menuItemChosen) {
               gameState = 8;
               this.loadingStep = 0;
               this.loadingStepCount = 32;
               return;
            }
            break;
         case 8:
            int j4 = this.loadingStep++;
            gamecanvas1 = this;
            if (j4 < 21) {
               if (j4 == 0) {
                  gamecanvas1.sound.openPack();
               }

               gamecanvas1.sound.loadSound(j4);
               if (j4 == 20) {
                  gamecanvas1.sound.closePack();
               }

               System.gc();
            } else {
               switch (j4) {
                  case 21:
                     sprites[9] = loadSprite("/cm.f", 7);
                     break;
                  case 22:
                     sprites[0] = loadSprite("/ui.f", 2);
                     break;
                  case 23:
                     demoUiImages[0] = loadModuleImages("/demoui.f", 0, 0);
                     demoUiImages[1] = loadModuleImages("/demoui.f", 0, 1);
                     break;
                  case 24:
                     gamecanvas1.sound.startThread();
                     gameState = 9;
                     gamecanvas1.openMenu(-1);
                     (unusedBufferA = new StringBuffer(stageNames[0])).delete(unusedBufferA.length() - 1, unusedBufferA.length());
                     (unusedBufferB = new StringBuffer(stageNames[11])).delete(unusedBufferB.length() - 1, unusedBufferB.length());
                     textBuffer = new StringBuffer("1");
               }
            }

            this.resetStateTimer();
            return;
         case 9:
            try {
               this.loadSave();
               int i4;
               switch (i4 = this.loadingStepCount == 8 ? this.loadingStep : this.loadingStep - 24) {
                  case 0:
                     createFonts();
                     System.gc();
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  default:
                     break;
                  case 6:
                     if (splashTop == null) {
                        splashTop = loadImage("/spl.f", 0);
                     }

                     if (splashMiddle == null) {
                        splashMiddle = loadImage("/spl.f", 1);
                     }

                     if (splashBottom == null) {
                        splashBottom = loadImage("/spl.f", 2);
                     }
                     break;
                  case 7:
                     if (sprites[18] == null) {
                        sprites[18] = loadSprite("/ui.f", 3);
                     }
               }

               i4++;
               this.loadingStep++;
               this.menuGameStartedStale = true;
               if (i4 == 8) {
                  if (this.titleShownOnce) {
                     gameState = 4;
                     if (this.menu == -1) {
                        this.subState = 0;
                        this.openMenu(0);
                     } else {
                        this.subState = 2;
                     }
                  } else {
                     gameState = 30;
                     this.titleShownOnce = true;
                  }

                  this.sound.play(19);
               }
            } catch (Exception exception1) {
            }

            this.resetStateTimer();
            return;
         case 11:
            this.freeStageOnLoad = true;
            this.unloadMapOnLoad = true;
            gameState = 5;
            this.tipVisible = true;
            this.resetStateTimer();
            return;
         case 15:
            gamecanvas1 = this;
            if (this.unlockedWorldNotice > 0 || gamecanvas1.unlockedArmorNotice > 0) {
               StringBuffer stringbuffer = new StringBuffer();
               if (gamecanvas1.unlockedWorldNotice > 0) {
                  label959: {
                     stringbuffer.append(strings[124]).append("\n");
                     byte[] abyte;
                     byte b9;
                     byte b12;
                     byte b13;
                     switch (gamecanvas1.unlockedWorldNotice) {
                        case 1:
                           stringbuffer.append(strings[4]);
                           abyte = saveData;
                           b9 = 2;
                           b12 = saveData[2];
                           b13 = 8;
                           break;
                        case 2:
                           stringbuffer.append(strings[64]);
                           saveData[2] = (byte)(saveData[2] | 8);
                           abyte = saveData;
                           b9 = 2;
                           b12 = saveData[2];
                           b13 = 16;
                           break;
                        default:
                           break label959;
                     }

                     abyte[b9] = (byte)(b12 | b13);
                  }

                  gamecanvas1.writeSave();
                  gamecanvas1.unlockedWorldNotice = 0;
               }

               if (gamecanvas1.unlockedArmorNotice > 0) {
                  if (stringbuffer.length() > 0) {
                     stringbuffer.append("\n\n");
                  }

                  stringbuffer.append(strings[33]).append("\n").append(strings[120 + gamecanvas1.unlockedArmorNotice - 1]);
                  gamecanvas1.unlockedArmorNotice = 0;
               }

               if (stringbuffer.length() > 0) {
                  gamecanvas1.showNotice(stringbuffer.toString(), -1, -1, 5000, 4273165, 0);
               }
            }

            return;
         case 16:
            this.creatingNewGame = true;
            this.startStage();
            keysDown = 0;
            return;
         case 17:
            gamecanvas1 = this;
            switch (this.subState) {
               case 0:
                  if (stateTicks > 40 || gamecanvas1.resultsSkipped) {
                     gamecanvas1.subState++;
                     gamecanvas1.resetStateTimer();
                     return;
                  }
                  break;
               case 1:
                  if (stateTicks > gamecanvas1.stageDiamonds << 1 && stateTicks > 40 || gamecanvas1.resultsSkipped) {
                     gamecanvas1.subState++;
                     gamecanvas1.resetStateTimer();
                     return;
                  }
                  break;
               case 2:
                  if (stateTicks > 40 || gamecanvas1.resultsSkipped) {
                     gamecanvas1.subState++;
                     gamecanvas1.resetStateTimer();
                     return;
                  }
                  break;
               case 3:
                  if (stateTicks > 10 || gamecanvas1.resultsSkipped) {
                     gamecanvas1.subState++;
                     gamecanvas1.resetStateTimer();
                     return;
                  }
                  break;
               case 4:
                  if (stateTicks > 10 || gamecanvas1.resultsSkipped) {
                     gamecanvas1.subState++;
                     gamecanvas1.resetStateTimer();
                     gamecanvas1.resultsSkipped = false;
                  }
               case 5:
            }

            return;
         case 20:
            if (stateTicks > 30) {
               this.cheatSkipExitB = true;
               this.freeStageOnLoad = true;
               this.unloadMapOnLoad = true;
               this.openWorldMap();
               return;
            }
            break;
         case 21:
            int l3 = this.loadingStep++;
            gamecanvas1 = this;

            try {
               label920:
               switch (l3) {
                  case 0:
                     if (gamecanvas1.saveResultOnLoad) {
                        gamecanvas1.saveStageResult();
                     }
                     break;
                  case 1:
                     if (gamecanvas1.freeStageOnLoad) {
                        gamecanvas1.freeStageResources();
                     }
                     break;
                  case 2:
                     if (gamecanvas1.mapSpritesLoaded) {
                        gamecanvas1.mapSpritesLoaded = false;
                        unloadSprite(sprites[17], true);
                        sprites[17] = null;
                        unloadSprite(sprites[10], true);
                        sprites[10] = null;
                        unloadSprite(sprites[46], true);
                        sprites[46] = null;
                        unloadSprite(sprites[55], true);
                        sprites[55] = null;
                        imageSets[8] = null;
                        unloadSprite(sprites[59], true);
                        imageSets[3] = null;
                        unloadSprite(sprites[17], true);
                        sprites[17] = null;
                        System.gc();
                     }
                     break;
                  case 3:
                     if (gamecanvas1.unloadMapOnLoad) {
                        gamecanvas1.unloadMapResources();
                     }
                     break;
                  case 4:
                     FontRenderer.setColor(16777215);
                     textBuffer.delete(0, textBuffer.length());
                     switch (gamecanvas1.world) {
                        case 0:
                           textBuffer.append("/map_angkor.out");
                           break;
                        case 1:
                           textBuffer.append("/map_scotland.out");
                           break;
                        case 2:
                           textBuffer.append("/map_tibet.out");
                     }

                     mapNodes = new long[12][12];
                     gamecanvas1.mapPathPoints = new int[20];
                     gamecanvas1.loadMapFile(textBuffer.toString());
                     break;
                  case 5:
                     sprites[17] = loadSprite("/ms.f", 0);
                     sprites[23] = loadSprite("/ms.f", 1);
                     break;
                  case 6:
                     Sprite[] asprite;
                     byte b8;
                     String s1;
                     byte b15;
                     switch (gamecanvas1.world) {
                        case 0:
                           asprite = sprites;
                           b8 = 24;
                           s1 = "/ms.f";
                           b15 = 2;
                           break;
                        case 1:
                           asprite = sprites;
                           b8 = 25;
                           s1 = "/ms.f";
                           b15 = 3;
                           break;
                        case 2:
                           asprite = sprites;
                           b8 = 26;
                           s1 = "/ms.f";
                           b15 = 4;
                           break;
                        default:
                           break label920;
                     }

                     asprite[b8] = loadSprite(s1, b15);
                     break;
                  case 7:
                     if (sprites[54] == null) {
                        sprites[54] = loadSprite("/mmv.f", 1);
                     }

                     gamecanvas1.crystalHalfWidth = frameWidth(sprites[54], 0) >> 1;
                     gamecanvas1.crystalHalfHeight = frameHeight(sprites[54], 0) >> 1;
                     break;
                  case 8:
                     if (sprites[53] == null) {
                        sprites[53] = loadSprite("/mmv.f", 2);
                     }
                     break;
                  case 9:
                     if (sprites[52] == null) {
                        sprites[52] = loadSprite("/mmv.f", 3);
                     }
                     break;
                  case 10:
                     if (gamecanvas1.stageJustUnlocked) {
                        GameCanvas gamecanvas13 = gamecanvas1;
                        gamecanvas1.mapUnlockFrame = 0;
                        gamecanvas13.mapPathLength = 0;
                        gamecanvas13.mapPathShown = 0;
                        gamecanvas13.mapUnlockAnimDone = false;
                        gamecanvas13.mapPathDotsVisible = true;
                        gamecanvas13.mapUnlockDone = false;
                        gamecanvas13.mapPathBlinkDelay = 2;
                        gamecanvas13.mapWalkerBlinkDelay = 2;
                        gamecanvas13.mapPathReversed = false;
                     }
                  case 11:
                  case 12:
                  case 13:
                  default:
                     break;
                  case 14:
                     if (gamecanvas1.cheatSkipExitB) {
                        gamecanvas1.cheatSkipExitB = false;
                     }

                     GameCanvas gamecanvas12 = gamecanvas1;
                     gamecanvas1.mapCursorX = -1;
                     if (!gamecanvas12.stageJustUnlocked) {
                        gamecanvas12.stage = unlockedStage;
                     }

                     gamecanvas12.stageJustUnlocked = false;

                     for (int j8 = 0; j8 < 12; j8++) {
                        for (int j10 = 0; j10 < 12; j10++) {
                           long k10;
                           if ((k10 = mapNodes[j8][j10]) != 0L) {
                              int i9 = longBits(k10, (byte)6, (byte)5);
                              int i15;
                              int l15;
                              byte b11;
                              if ((gamecanvas12.getStageFlags(gamecanvas12.world, i9) & 64) == 0 && i9 != 0) {
                                 i15 = j8;
                                 l15 = j10;
                                 b11 = 1;
                              } else {
                                 i15 = j8;
                                 l15 = j10;
                                 b11 = 0;
                              }

                              setMapNodeBits(i15, l15, b11, (byte)0, (byte)3);
                              if (i9 == gamecanvas12.stage) {
                                 gamecanvas12.mapCursorX = j8;
                                 gamecanvas12.mapCursorY = j10;
                              }

                              if (i9 == unlockedStage) {
                                 gamecanvas12.mapTargetX = j8;
                                 gamecanvas12.mapTargetY = j10;
                              }

                              gamecanvas12.mapWalkFacingLeft = false;
                           }
                        }
                     }

                     gameState = 15;
               }
            } catch (Exception exception4) {
            }

            this.resetStateTimer();
            return;
         case 22:
            gamecanvas1 = this;
            switch (this.subState) {
               case 0:
                  gamecanvas1.creditsScroll = 0;
                  gamecanvas1.creditsFirstLine = 0;
                  gamecanvas1.subState = 1;
                  return;
               case 1:
                  creditsText = readPackEntry("/cr.f", 0);
                  int k3 = 0;

                  while (k3 < creditsText.length) {
                     if (creditsText[k3] == 92 && creditsText[k3 + 1] == 110) {
                        creditsText[k3++] = 10;
                        creditsText[k3++] = 32;
                     } else {
                        k3++;
                     }
                  }

                  k3 = 0;

                  while (creditsText[k3] != 36) {
                     k3++;
                  }

                  for (int i6 = 0; i6 < GloftDIRU.versionBytes.length; k3++) {
                     creditsText[k3] = GloftDIRU.versionBytes[i6];
                     i6++;
                  }

                  gamecanvas1.subState = 2;
                  return;
               case 2:
                  label890: {
                     GameCanvas gamecanvas18;
                     int k15;
                     label889: {
                        byte b10;
                        label888: {
                           if (isKeyDown(4097)) {
                              if (gamecanvas1.creditsScroll >= 180) {
                                 gamecanvas1.creditsScroll -= 3;
                                 if (gamecanvas1.creditsScroll >= 180) {
                                    break label890;
                                 }

                                 gamecanvas18 = gamecanvas1;
                                 k15 = 180;
                                 break label889;
                              }
                           } else {
                              if (isKeyDown(262146)) {
                                 gamecanvas18 = gamecanvas1;
                                 k15 = gamecanvas1.creditsScroll;
                                 b10 = 3;
                                 break label888;
                              }

                              if (isKeyDown(64)) {
                                 if (gamecanvas1.endingReached) {
                                    gamecanvas1.loadingStep = 0;
                                    gamecanvas1.loadingStepCount = 8;
                                    gameState = 9;
                                    gamecanvas1.endingReached = false;
                                 } else {
                                    gamecanvas1.subState = 3;
                                 }
                                 break label890;
                              }
                           }

                           gamecanvas18 = gamecanvas1;
                           k15 = gamecanvas1.creditsScroll;
                           b10 = 1;
                        }

                        k15 += b10;
                     }

                     gamecanvas18.creditsScroll = k15;
                  }

                  int l5 = -gamecanvas1.creditsScroll;

                  int j3;
                  for (j3 = 0; j3 < creditsText.length && l5 <= -272; j3++) {
                     if (creditsText[j3] == 10) {
                        l5 += 17;
                     }
                  }

                  gamecanvas1.creditsFirstLine = j3;
                  if (gamecanvas1.creditsFirstLine >= creditsText.length) {
                     gamecanvas1.creditsFirstLine = 0;
                     gamecanvas1.creditsScroll = 0;
                     return;
                  }
                  break;
               case 3:
                  creditsText = null;
                  System.gc();
                  gameState = 4;
                  gamecanvas1.subState = 2;
                  gamecanvas1.openMenu(0);
                  gamecanvas1.sound.play(19);
            }

            return;
         case 27:
            this.updateSeal();
            return;
         case 28:
            try {
               this.loadSealStep(this.loadingStep);
               this.loadingStep++;
               if (this.loadingStep == 11) {
                  gameState = 27;
               }
            } catch (Exception exception) {
            }

            this.resetStateTimer();
            return;
         case 29:
            this.updateEnding();
            break;
         case 31:
            return;
         case 35:
            int i3 = this.loadingStep++;
            gamecanvas1 = this;
            switch (i3) {
               case 0:
                  newDiamondTotal = 0;
                  newRedDiamondTotal = 0;
                  newSaveFlags = 0;
                  gamecanvas1.stageAwardsBefore = gamecanvas1.getStageFlags(gamecanvas1.world, gamecanvas1.stage);
                  gamecanvas1.stageAwards = 0;
                  break;
               case 1:
                  newDiamondTotal = (newDiamondTotal = readShort(saveData, 4)) + gamecanvas1.stageDiamonds;
                  gamecanvas1.resultsSkipped = false;
                  break;
               case 2:
                  saveData[4] = (byte)newDiamondTotal;
                  saveData[5] = (byte)(newDiamondTotal >> 8);
                  break;
               case 3:
                  newRedDiamondTotal = (newRedDiamondTotal = readShort(saveData, 6)) + gamecanvas1.stageRedDiamonds;
                  break;
               case 4:
                  saveData[6] = (byte)newRedDiamondTotal;
                  saveData[7] = (byte)(newRedDiamondTotal >> 8);
                  break;
               case 5:
                  newSaveFlags = saveData[2];
                  gamecanvas1.unlockedWorldNotice = 0;
                  if ((newSaveFlags & 8) == 0 && newRedDiamondTotal >= Constants.worldUnlockRedDiamonds[1]) {
                     gamecanvas1.unlockedWorldNotice = 1;
                  } else if ((newSaveFlags & 16) == 0 && newRedDiamondTotal >= Constants.worldUnlockRedDiamonds[2]) {
                     gamecanvas1.unlockedWorldNotice = 2;
                  }
                  break;
               case 6:
                  try {
                     byte b3 = 4;
                     String s = "/ui.f";
                     imageSets[28] = loadModuleImages("/ui.f", 4, 0);
                  } catch (Exception exception3) {
                  }
                  break;
               case 7:
                  gamecanvas1.addStageFlags(gamecanvas1.world, gamecanvas1.stage, (byte)2);
                  gamecanvas1.unlockedArmorNotice = 0;
                  break;
               case 8:
                  int k5;
                  int j7 = k5 = (newSaveFlags & 224) >> 5;

                  while (j7 < 4 && newDiamondTotal >= Constants.armorDiamondThresholds[j7]) {
                     j7++;
                  }

                  if (k5 < j7) {
                     saveData[2] = (byte)(saveData[2] & -225);
                     saveData[2] = (byte)(saveData[2] | j7 << 5 & 224);
                     gamecanvas1.writeSave();
                     gamecanvas1.unlockedArmorNotice = j7;
                  }
               case 9:
                  gamecanvas1.commitTakenTreasures();
                  break;
               case 10:
                  gamecanvas1.resetStateTimer();
                  System.gc();
                  break;
               case 11:
                  if (gamecanvas1.lives < 99 && gamecanvas1.stageDiamonds == gamecanvas1.stageDiamondTotal && (gamecanvas1.stageAwardsBefore & 4) == 0) {
                     gamecanvas1.addStageFlags(gamecanvas1.world, gamecanvas1.stage, (byte)4);
                     gamecanvas1.stageAwards = (byte)(gamecanvas1.stageAwards | 4);
                     gamecanvas1.lives++;
                  }

                  if (gamecanvas1.lives < 99 && gamecanvas1.stageRedDiamonds == gamecanvas1.stageRedDiamondTotal && (gamecanvas1.stageAwardsBefore & 8) == 0) {
                     gamecanvas1.addStageFlags(gamecanvas1.world, gamecanvas1.stage, (byte)8);
                     gamecanvas1.stageAwards = (byte)(gamecanvas1.stageAwards | 8);
                     gamecanvas1.lives++;
                  }

                  if (gamecanvas1.lives < 99 && gamecanvas1.stageHits == 0 && (gamecanvas1.stageAwardsBefore & 16) == 0) {
                     gamecanvas1.addStageFlags(gamecanvas1.world, gamecanvas1.stage, (byte)16);
                     gamecanvas1.stageAwards = (byte)(gamecanvas1.stageAwards | 16);
                     gamecanvas1.lives++;
                  }

                  if (gamecanvas1.lives < 99 && gamecanvas1.stageRetries == 0 && (gamecanvas1.stageAwardsBefore & 32) == 0) {
                     gamecanvas1.addStageFlags(gamecanvas1.world, gamecanvas1.stage, (byte)32);
                     gamecanvas1.stageAwards = (byte)(gamecanvas1.stageAwards | 32);
                     gamecanvas1.lives++;
                  }

                  GameCanvas gamecanvas14 = gamecanvas1;
                  int j11 = gamecanvas1.stage;
                  int l8 = 0;

                  long l14;
                  label1008:
                  while (true) {
                     if (l8 >= 12) {
                        l14 = -1L;
                        break;
                     }

                     for (int k11 = 0; k11 < 12; k11++) {
                        long i13;
                        if ((i13 = mapNodes[l8][k11]) != 0L && longBits(i13, (byte)6, (byte)5) == j11) {
                           l14 = i13;
                           break label1008;
                        }
                     }

                     l8++;
                  }

                  long i10 = l14;
                  if ((gamecanvas14.stage == 0 || longBits(i10, (byte)11, (byte)3) > 1)
                     && i10 >= 0L
                     && (gamecanvas14.getStageFlags(gamecanvas14.world, gamecanvas14.stage + 1) & 64) == 0) {
                     unlockedStage = gamecanvas14.stage + 1;
                     gamecanvas14.addStageFlags(gamecanvas14.world, unlockedStage, (byte)64);
                     gamecanvas14.stageJustUnlocked = true;
                  } else {
                     unlockedStage = gamecanvas14.stage;
                  }

                  gamecanvas14.saveProgress(false);
                  gamecanvas1.saveStageResult();
                  gamecanvas1.saveResultOnLoad = false;
                  gamecanvas1.unloadMapOnLoad = true;
            }

            this.resetStateTimer();
            if (this.loadingStep == 12) {
               gameState = 17;
               this.subState = 0;
               this.playSound(15);
               return;
            }
      }
   }

   private void updateEnding() {
      switch (this.endingPhase) {
         case 0:
            if (System.currentTimeMillis() - this.stateStartTime >= 3000L) {
               this.endingPhase++;
               this.resetStateTimer();
               return;
            }
            break;
         case 1:
            GameCanvas gamecanvas2;
            boolean flag;
            if (stateTicks % 6 >= 3) {
               this.endingFlashColor += 1677721;
               gamecanvas2 = this;
               flag = false;
            } else {
               gamecanvas2 = this;
               flag = true;
            }

            gamecanvas2.fullRedraw = flag;
            if (System.currentTimeMillis() - this.stateStartTime >= 5000L) {
               this.endingPhase++;
               this.fullRedraw = true;
               this.resetStateTimer();
               return;
            }
            break;
         case 2:
            GameCanvas gamecanvas;
            label62: {
               gamecanvas = this;
               GameCanvas gamecanvas3;
               byte b1;
               if (this.endingShakeY >= gamecanvas.endingShakeLimitY) {
                  gamecanvas3 = gamecanvas;
                  b1 = -1;
               } else {
                  if (gamecanvas.endingShakeY > -gamecanvas.endingShakeLimitY) {
                     break label62;
                  }

                  gamecanvas3 = gamecanvas;
                  b1 = 1;
               }

               gamecanvas3.endingShakeDirY = b1;
            }

            label56: {
               gamecanvas.endingShakeY = gamecanvas.endingShakeY + gamecanvas.endingShakeDirY * gamecanvas.endingShakeSpeed;
               GameCanvas gamecanvas1;
               byte b0;
               if (gamecanvas.endingShakeX >= gamecanvas.endingShakeLimitX) {
                  gamecanvas1 = gamecanvas;
                  b0 = -1;
               } else {
                  if (gamecanvas.endingShakeY > -gamecanvas.endingShakeLimitX) {
                     break label56;
                  }

                  gamecanvas1 = gamecanvas;
                  b0 = 1;
               }

               gamecanvas1.endingShakeDirX = b0;
            }

            gamecanvas.endingShakeX += 0;
            if (System.currentTimeMillis() - this.stateStartTime >= 10000L) {
               this.endingShakeX = 0;
               this.endingShakeY = 0;
               this.resetStateTimer();
               this.endingPhase++;
               return;
            }
            break;
         case 3:
            if (System.currentTimeMillis() - this.stateStartTime >= 1000L) {
               this.endingPhase++;
               return;
            }
            break;
         case 4:
            if (System.currentTimeMillis() - this.stateStartTime >= 1000L) {
               this.endingPhase++;
               return;
            }
            break;
         case 5:
            if (System.currentTimeMillis() - this.stateStartTime >= 1000L) {
               this.endingPhase++;
               return;
            }
            break;
         case 6:
            if (System.currentTimeMillis() - this.stateStartTime >= 1000L) {
               this.endingPhase++;
               this.resetStateTimer();
               return;
            }
            break;
         case 7:
            if (System.currentTimeMillis() - this.stateStartTime >= 1000L) {
               TreasureRain.grid = new byte[15][10];
               this.endingPhase++;
               TreasureRain.spawnRow(3);
               this.sound.play(19);
               this.resetStateTimer();
               return;
            }
            break;
         case 8:
            TreasureRain.update(3);
            this.fullRedraw = true;
            if (System.currentTimeMillis() - this.stateStartTime >= 15000L) {
               this.endingPhase++;
               this.resetStateTimer();
               return;
            }
            break;
         case 9:
            this.fullRedraw = true;
            TreasureRain.update(3);
            if (System.currentTimeMillis() - this.stateStartTime >= 12000L) {
               this.endingPhase++;
               this.resetStateTimer();
               return;
            }
            break;
         case 10:
            this.freeStageResources();
            this.openMenu(0);
            gameState = 22;
            this.subState = 0;
            this.sound.stop();
      }
   }

   private void loadSealStep(int var1) {
      switch (var1) {
         case 0:
            if (this.unloadCutscenesOnLoad) {
               this.unloadCutscenes();
               return;
            }
            break;
         case 1:
            if (this.unloadMapOnLoad) {
               this.unloadMapResources();
               return;
            }
            break;
         case 2:
            if (this.reloadLivesOnLoad) {
               this.loadLives();
               return;
            }
            break;
         case 3:
            if (this.gcOnLoad) {
               System.gc();
               this.gcOnLoad = false;
               return;
            }
            break;
         case 4:
            this.freeStageResources();
            if (sprites[10] == null) {
               byte b8 = 0;
               String s15 = "/mmv.f";
               boolean flag7 = false;
               b8 = b8;
               String s16 = "/mmv.f";
               sprites[10] = loadSprite("/mmv.f", b8, 0, 0);
            }

            if (sprites[46] == null) {
               byte b9 = 5;
               String s17 = "/mmv.f";
               boolean flag8 = false;
               b9 = b9;
               String s18 = "/mmv.f";
               sprites[46] = loadSprite("/mmv.f", b9, 0, 0);
            }

            this.sealX = 320 - frameWidth(sprites[10], 0) >> 1;
            this.sealY = (240 - frameHeight(sprites[10], 0) - 48 >> 1) - 20;
            return;
         case 5:
            if (sprites[55] == null) {
               byte b6 = 4;
               String s11 = "/mmv.f";
               boolean flag5 = false;
               b6 = b6;
               String s12 = "/mmv.f";
               sprites[55] = loadSprite("/mmv.f", b6, 0, 0);
               this.cursorAnimLength = animLength(sprites[55], 0);
               paintSprite = sprites[55];
            }

            if (sprites[18] == null) {
               byte b7 = 3;
               String s13 = "/ui.f";
               boolean flag6 = false;
               b7 = b7;
               String s14 = "/ui.f";
               sprites[18] = loadSprite("/ui.f", b7, 0, 0);
               return;
            }
            break;
         case 6:
            if (sprites[54] == null) {
               byte b5 = 1;
               String s9 = "/mmv.f";
               boolean flag4 = false;
               b5 = b5;
               String s10 = "/mmv.f";
               sprites[54] = loadSprite("/mmv.f", b5, 0, 0);
            }

            this.crystalHalfWidth = frameWidth(sprites[54], 0) >> 1;
            this.crystalHalfHeight = (frameHeight(sprites[54], 0) >> 1) - 20;
            return;
         case 7:
            if (sprites[53] == null) {
               byte b4 = 2;
               String s7 = "/mmv.f";
               boolean flag3 = false;
               b4 = b4;
               String s8 = "/mmv.f";
               sprites[53] = loadSprite("/mmv.f", b4, 0, 0);
               return;
            }
            break;
         case 8:
            if (sprites[52] == null) {
               byte b3 = 3;
               String s5 = "/mmv.f";
               boolean flag2 = false;
               b3 = b3;
               String s6 = "/mmv.f";
               sprites[52] = loadSprite("/mmv.f", b3, 0, 0);
               return;
            }
            break;
         case 9:
            try {
               String s19 = "/" + 0 + ".f";
               boolean flag1 = false;
               byte b2 = 3;
               Sprite sprite = loadSprite(s19, b2, 0, 0);
               imageSets[8] = sprite.moduleImages[0];
               flag1 = false;
               b2 = 2;
               String s2 = "/cm.f";
               Sprite sprite1;
               (sprite1 = loadSprite("/cm.f", b2, 0, 0)).buildModuleImages(0, 0, -1, -1);
               sprite1.buildModuleImages(1, 0, 0, -1);
               sprites[59] = sprite1;
               TreasureRain.animFrameCount = sprite1.moduleImages[0].length;
               sprite1.moduleData = null;
               if (sprites[17] == null) {
                  b2 = 0;
                  String s3 = "/ms.f";
                  flag1 = false;
                  b2 = b2;
                  String s4 = "/ms.f";
                  sprites[17] = loadSprite("/ms.f", b2, 0, 0);
               }

               return;
            } catch (Exception exception) {
               return;
            }
         case 10:
            if (sprites[9] == null) {
               byte b0 = 7;
               String s = "/cm.f";
               boolean flag = false;
               b0 = b0;
               String s1 = "/cm.f";
               sprites[9] = loadSprite("/cm.f", b0, 0, 0);
            }

            this.crystalAnimLength = animLength(sprites[9], 5);
            byte b1 = saveData[2];

            for (int i = 0; i < 3; i++) {
               if ((b1 & 1 << i) != 0) {
                  worldCrystalEarned[i] = true;
               }
            }

            label88: {
               this.flyingCrystalX = 10;
               this.flyingCrystalY = 10;
               boolean[] aboolean1;
               byte b11;
               boolean flag10;
               if (((b1 = saveData[1]) & 1) != 0) {
                  aboolean1 = crystalInserted;
                  b11 = 1;
                  flag10 = true;
               } else {
                  if (readShort(saveData, 6) >= Constants.worldUnlockRedDiamonds[1]) {
                     saveData[1] = (byte)(saveData[1] | 1);
                     this.writeSave();
                     this.crystalsPlaced = 1;
                     this.sealLocation = 1;
                     break label88;
                  }

                  aboolean1 = crystalInserted;
                  b11 = 1;
                  flag10 = false;
               }

               aboolean1[b11] = flag10;
            }

            label82: {
               boolean[] aboolean;
               byte b10;
               boolean flag9;
               if ((b1 & 2) != 0) {
                  aboolean = crystalInserted;
                  b10 = 2;
                  flag9 = true;
               } else {
                  if (readShort(saveData, 6) >= Constants.worldUnlockRedDiamonds[2]) {
                     saveData[1] = (byte)(saveData[1] | 2);
                     this.writeSave();
                     this.crystalsPlaced = 2;
                     this.sealLocation = 2;
                     break label82;
                  }

                  aboolean = crystalInserted;
                  b10 = 2;
                  flag9 = false;
               }

               aboolean[b10] = flag9;
            }

            this.sealCursorX = Constants.sealLocationOffsets[this.sealLocation << 1];
            this.sealCursorY = Constants.sealLocationOffsets[(this.sealLocation << 1) + 1] - 20;
            this.sealCursorPrevX = this.sealCursorX;
            this.sealCursorPrevY = this.sealCursorY;
            this.updateSealCaption();
            this.sealDirty = true;
            this.fullRedraw = true;
      }
   }

   private void updateSealCaption() {
      GameCanvas gamecanvas;
      StringBuffer stringbuffer;
      String s;
      if (this.sealLocation == 3) {
         gamecanvas = this;
         stringbuffer = new StringBuffer();
         s = strings[45];
      } else if (isWorldUnlocked(this.sealLocation)) {
         gamecanvas = this;
         stringbuffer = new StringBuffer();
         s = strings[45];
      } else {
         gamecanvas = this;
         stringbuffer = new StringBuffer().append(Constants.worldUnlockRedDiamonds[this.sealLocation]).append(" ");
         s = strings[48].toLowerCase();
      }

      gamecanvas.sealCaption = stringbuffer.append(s).append(" ").append(strings[77]).toString();
      String[] astring;
      byte b0;
      switch (this.sealLocation) {
         case 0:
            gamecanvas = this;
            astring = strings;
            b0 = 1;
            break;
         case 1:
            gamecanvas = this;
            astring = strings;
            b0 = 4;
            break;
         case 2:
            gamecanvas = this;
            astring = strings;
            b0 = 64;
            break;
         case 3:
            gamecanvas = this;
            astring = strings;
            b0 = 63;
            break;
         default:
            return;
      }

      gamecanvas.sealCaptionTarget = astring[b0];
   }

   private static boolean isWorldUnlocked(int var0) {
      return var0 != 0 && var0 != 3 ? readShort(saveData, 6) >= Constants.worldUnlockRedDiamonds[var0] : true;
   }

   private void updateSeal() {
      if (!this.frameSkipped) {
         this.sealCursorPrevX = this.sealCursorX;
         this.sealCursorPrevY = this.sealCursorY;
      }

      if (this.sealCursorMoving) {
         keysDown = 0;
         int i = this.sealCursorTargetX - this.sealCursorX;
         int j = this.sealCursorTargetY - this.sealCursorY;
         this.sealCursorX = this.sealCursorX + i / (8 - this.sealCursorStep);
         this.sealCursorY = this.sealCursorY + j / (8 - this.sealCursorStep);
         this.sealCursorStep++;
         if (this.sealCursorStep == 8) {
            this.sealCursorX = this.sealCursorTargetX;
            this.sealCursorY = this.sealCursorTargetY;
            this.sealCursorMoving = false;
            this.sealCursorStep = 0;
            this.sealDirty = true;
            this.updateSealCaption();
            return;
         }
      } else {
         label32:
         switch (this.sealInputDirection) {
            case -1:
               break;
            case 4:
               switch (this.sealLocation) {
                  case 0:
                     this.mapSpritesLoaded = true;
                     this.unloadMapOnLoad = true;
                     this.sound.stop();
                     this.world = 0;
                     this.hasEnemies = false;
                     gameState = 15;
                     unlockedStage = getWorldProgress(this.world);
                     this.openWorldMap();
                     break label32;
                  case 1:
                     if (isWorldUnlocked(this.sealLocation)) {
                        this.mapSpritesLoaded = true;
                        this.unloadMapOnLoad = true;
                        this.sound.stop();
                        this.world = 1;
                        this.stage = 0;
                        gameState = 15;
                        unlockedStage = getWorldProgress(this.world);
                        this.openWorldMap();
                        this.hasCompass = true;
                        this.hasEnemies = false;
                        if (saveData[9] < 1) {
                           saveData[9] = 1;
                        }
                     }
                     break label32;
                  case 2:
                     if (isWorldUnlocked(this.sealLocation)) {
                        this.mapSpritesLoaded = true;
                        this.unloadMapOnLoad = true;
                        this.sound.stop();
                        this.world = 2;
                        this.stage = 0;
                        this.hasEnemies = false;
                        gameState = 15;
                        unlockedStage = getWorldProgress(this.world);
                        this.openWorldMap();
                        this.hasCompass = true;
                        if (saveData[9] < 2) {
                           saveData[9] = 2;
                        }
                     }
                     break label32;
                  case 3:
                     gameState = 18;
                  default:
                     break label32;
               }
            default:
               byte b0;
               if ((b0 = Constants.sealNavigation[this.sealInputDirection][this.sealLocation]) != -1) {
                  this.sealLocation = b0;
                  this.sealCursorMoving = true;
                  this.sealCursorTargetX = Constants.sealLocationOffsets[this.sealLocation << 1];
                  this.sealCursorTargetY = Constants.sealLocationOffsets[(this.sealLocation << 1) + 1] - 20;
               }
         }

         this.sealInputDirection = -1;
      }
   }

   private static String[] loadStrings(int var0) {
      String[] astring = new String[128];

      for (int i = 0; i <= 127; i++) {
         try {
            astring[i] = Text.get(i);
         } catch (Exception exception) {
            astring[i] = "E";
         }
      }

      return astring;
   }

   private static String[] loadTutorialStrings() {
      String[] astring = new String[39];

      for (int i = 0; i < 39; i++) {
         try {
            switch (i) {
               case 0:
                  astring[i] = Text.get(79);
                  break;
               case 1:
                  astring[i] = Text.get(80);
                  break;
               case 2:
                  astring[i] = Text.get(91);
                  break;
               case 3:
                  astring[i] = Text.get(102);
                  break;
               case 4:
                  astring[i] = Text.get(112);
                  break;
               case 5:
                  astring[i] = Text.get(113);
                  break;
               case 6:
                  astring[i] = Text.get(114);
                  break;
               case 7:
                  astring[i] = Text.get(115);
                  break;
               case 8:
                  astring[i] = Text.get(116);
                  break;
               case 9:
                  astring[i] = Text.get(117);
                  break;
               case 10:
                  astring[i] = Text.get(81);
                  break;
               case 11:
                  astring[i] = Text.get(82);
                  break;
               case 12:
                  astring[i] = Text.get(83);
                  break;
               case 13:
                  astring[i] = Text.get(84);
                  break;
               case 14:
                  astring[i] = Text.get(85);
                  break;
               case 15:
                  astring[i] = Text.get(86);
                  break;
               case 16:
                  astring[i] = Text.get(87);
                  break;
               case 17:
                  astring[i] = Text.get(88);
                  break;
               case 18:
                  astring[i] = Text.get(89);
                  break;
               case 19:
                  astring[i] = Text.get(90);
                  break;
               case 20:
                  astring[i] = Text.get(92);
                  break;
               case 21:
                  astring[i] = Text.get(93);
                  break;
               case 22:
                  astring[i] = Text.get(94);
                  break;
               case 23:
                  astring[i] = Text.get(95);
                  break;
               case 24:
                  astring[i] = Text.get(96);
                  break;
               case 25:
                  astring[i] = Text.get(97);
                  break;
               case 26:
                  astring[i] = Text.get(98);
                  break;
               case 27:
                  astring[i] = Text.get(99);
                  break;
               case 28:
                  astring[i] = Text.get(100);
                  break;
               case 29:
                  astring[i] = Text.get(101);
                  break;
               case 30:
                  astring[i] = Text.get(103);
                  break;
               case 31:
                  astring[i] = Text.get(104);
                  break;
               case 32:
                  astring[i] = Text.get(105);
                  break;
               case 33:
                  astring[i] = Text.get(106);
                  break;
               case 34:
                  astring[i] = Text.get(107);
                  break;
               case 35:
                  astring[i] = Text.get(108);
                  break;
               case 36:
                  astring[i] = Text.get(109);
                  break;
               case 37:
                  astring[i] = Text.get(110);
                  break;
               case 38:
                  astring[i] = Text.get(111);
            }
         } catch (Exception exception) {
            astring[i] = "E";
         }
      }

      return astring;
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   private void loadStageStep() {
      label917: {
         label916: {
            label915: {
               label914: {
                  label913: {
                     label912: {
                        label911: {
                           label910: {
                              label909: {
                                 label919: {
                                    int i;
                                    try {
                                       i = this.loadingStep;
                                       if (this.creatingNewGame) {
                                          i -= 5;
                                       }

                                       this.loadingStep++;
                                       switch (i) {
                                          case 0:
                                             if (this.writeSaveOnLoad) {
                                                this.writeSave();
                                             }

                                             return;
                                          case 1:
                                             break label911;
                                          case 2:
                                             break label912;
                                          case 3:
                                             break label913;
                                          case 4:
                                             break label914;
                                          case 5:
                                             break label915;
                                          case 6:
                                             break label919;
                                          case 7:
                                             break label916;
                                          case 8:
                                             break label909;
                                          case 9:
                                             break label910;
                                          case 10:
                                             break label917;
                                       }
                                    } catch (Exception exception20) {
                                       return;
                                    }

                                    try {
                                       if ((i = i - 10 - 1) < 4) {
                                          Sprite sprite5;
                                          label793: {
                                             int j3 = i;
                                             GameCanvas gamecanvas3 = this;
                                             byte[] abyte2 = new byte[readInt(genPackDirectory, (j3 << 3) + 4)];
                                             gamecanvas3.stagePackStream.read(abyte2);
                                             (sprite5 = new Sprite()).load(abyte2, 0);
                                             sprite5.buildModuleImages(0, 0, -1, -1);
                                             Object object;
                                             byte b6;
                                             Object object1;
                                             switch (j3) {
                                                case 0:
                                                   if ((gamecanvas3.neededExtraSprites & 1L) == 0L) {
                                                      break label793;
                                                   }

                                                   object = sprites;
                                                   b6 = 60;
                                                   object1 = sprite5;
                                                   break;
                                                case 1:
                                                   if ((gamecanvas3.neededExtraSprites & 2L) == 0L) {
                                                      break label793;
                                                   }

                                                   object = sprites;
                                                   b6 = 16;
                                                   object1 = sprite5;
                                                   break;
                                                case 2:
                                                   imageSets[0] = null;
                                                   object = sprites;
                                                   b6 = 42;
                                                   object1 = sprite5;
                                                   break;
                                                case 3:
                                                   object = imageSets;
                                                   b6 = 8;
                                                   object1 = sprite5.moduleImages[0];
                                                   break;
                                                default:
                                                   break label793;
                                             }

                                             ((Object[])object)[b6] = object1;
                                          }

                                          sprite5.moduleData = null;
                                          this.genPackNumber = 0;
                                          return;
                                       }
                                    } catch (Exception exception13) {
                                       return;
                                    }

                                    try {
                                       i -= 4;
                                       if (i >= 0 && i < 43) {
                                          int i3 = i;
                                          GameCanvas gamecanvas2 = this;

                                          try {
                                             if (i3 % 10 == 0) {
                                                gamecanvas2.stagePackStream.close();
                                                gamecanvas2.stagePackStream = null;
                                                StringBuffer stringbuffer = new StringBuffer("/gen").append(gamecanvas2.genPackNumber).append(".f");
                                                gamecanvas2.stagePackStream = gamecanvas2.getClass().getResourceAsStream(stringbuffer.toString());
                                                gamecanvas2.genPackEntryCount = gamecanvas2.stagePackStream.read();
                                                genPackDirectory = new byte[gamecanvas2.genPackEntryCount << 3];
                                                gamecanvas2.stagePackStream.read(genPackDirectory);
                                                gamecanvas2.genPackSkipBytes = 0;
                                                gamecanvas2.genPackNumber++;
                                             }

                                             int l1 = i3 - (gamecanvas2.genPackNumber - 1) * 10;
                                             int k3 = readInt(genPackDirectory, (l1 << 3) + 4);
                                             if ((gamecanvas2.neededGenSprites & 1L << i3) != 0L) {
                                                if (gamecanvas2.genPackSkipBytes != 0) {
                                                   gamecanvas2.stagePackStream.skip(gamecanvas2.genPackSkipBytes);
                                                   gamecanvas2.genPackSkipBytes = 0;
                                                }

                                                byte[] abyte3 = new byte[k3];
                                                gamecanvas2.stagePackStream.read(abyte3);
                                                Sprite sprite1;
                                                (sprite1 = new Sprite()).load(abyte3, 0);
                                                if ((i3 != 28 || gamecanvas2.stageHasDoors9) && (i3 != 24 || gamecanvas2.stageHasWindPods)) {
                                                   sprite1.buildModuleImages(0, 0, -1, -1);
                                                }

                                                byte b0;
                                                byte b1;
                                                label800: {
                                                   b0 = -1;
                                                   b1 = -1;
                                                   byte b5;
                                                   switch (i3) {
                                                      case 0:
                                                         b0 = 33;
                                                         break label800;
                                                      case 1:
                                                         b0 = 34;
                                                         break label800;
                                                      case 2:
                                                         if (gamecanvas2.stageHasDoors9) {
                                                            imageSets[24] = sprite1.moduleImages[0];
                                                         }

                                                         if (gamecanvas2.stageHasDoors8) {
                                                            sprite1.buildModuleImages(1, 0, -1, -1);
                                                            imageSets[25] = sprite1.moduleImages[1];
                                                         }
                                                         break label800;
                                                      case 3:
                                                         b0 = 7;
                                                         break label800;
                                                      case 4:
                                                      case 21:
                                                         if (gamecanvas2.world != 2) {
                                                            b0 = 3;
                                                         }
                                                         break label800;
                                                      case 5:
                                                         b0 = 58;
                                                         break label800;
                                                      case 6:
                                                         b5 = 21;
                                                         break;
                                                      case 7:
                                                         if (gamecanvas2.world == 2) {
                                                            sprite1.moduleImages = null;
                                                            sprite1.buildModuleImages(1, 0, -1, -1);
                                                            sprite1.currentPalette = 1;
                                                         }

                                                         b0 = 20;
                                                         break label800;
                                                      case 8:
                                                         b0 = 5;
                                                         break label800;
                                                      case 9:
                                                         b0 = 12;
                                                         break label800;
                                                      case 10:
                                                         spriteInstances[4] = new SpriteInstance(sprite1, 0, 0, null);
                                                         spriteInstances[4].setAnim(0);
                                                         break label800;
                                                      case 11:
                                                         b0 = 11;
                                                         break label800;
                                                      case 12:
                                                         b5 = 10;
                                                         break;
                                                      case 13:
                                                         b0 = 1;
                                                         break label800;
                                                      case 14:
                                                         b5 = 6;
                                                         break;
                                                      case 15:
                                                      case 17:
                                                         if ((gamecanvas2.rollerVariants & 2) != 0) {
                                                            if (gamecanvas2.world == 2) {
                                                               sprite1.moduleImages = null;
                                                               sprite1.buildModuleImages(2, 0, -1, -1);
                                                               sprite1.currentPalette = 2;
                                                            }

                                                            b0 = 4;
                                                         }

                                                         if ((gamecanvas2.rollerVariants & 1) != 0) {
                                                            sprites[21] = new Sprite();
                                                            sprites[21].load(abyte3, 0);
                                                            sprites[21].buildModuleImages(1, 0, -1, -1);
                                                            sprites[21].currentPalette = 1;
                                                            sprites[21].moduleData = null;
                                                         }
                                                         break label800;
                                                      case 16:
                                                      case 18:
                                                         b0 = 37;
                                                         break label800;
                                                      case 19:
                                                         b5 = 7;
                                                         break;
                                                      case 20:
                                                         b5 = 13;
                                                         break;
                                                      case 22:
                                                         b0 = 8;
                                                         break label800;
                                                      case 23:
                                                         b0 = 30;
                                                         break label800;
                                                      case 24:
                                                         imageSets[15] = null;
                                                         sprite1.buildModuleImages(1, 0, -1, -1);
                                                         b0 = 57;
                                                         imageSets[14] = null;
                                                         break label800;
                                                      case 25:
                                                         b5 = 17;
                                                         break;
                                                      case 26:
                                                         slotSprite = sprite1;
                                                         break label800;
                                                      case 27:
                                                         b5 = 19;
                                                         break;
                                                      case 28:
                                                         if (gamecanvas2.stageHasDoors8) {
                                                            sprite1.buildModuleImages(1, 0, -1, -1);
                                                         }

                                                         b0 = 45;
                                                         break label800;
                                                      case 29:
                                                         b5 = 26;
                                                         break;
                                                      case 30:
                                                         b0 = 15;
                                                         break label800;
                                                      case 31:
                                                         b5 = 29;
                                                         break;
                                                      case 32:
                                                         b0 = 32;
                                                         break label800;
                                                      case 33:
                                                         b0 = 22;
                                                         break label800;
                                                      case 34:
                                                         b0 = 27;
                                                         break label800;
                                                      case 35:
                                                         b0 = 28;
                                                         break label800;
                                                      case 36:
                                                         b0 = 35;
                                                         break label800;
                                                      case 37:
                                                         b0 = 29;
                                                         break label800;
                                                      case 38:
                                                         b0 = 36;
                                                         break label800;
                                                      case 39:
                                                         b0 = 6;
                                                         break label800;
                                                      case 40:
                                                         b0 = 2;
                                                         break label800;
                                                      case 41:
                                                         b0 = 38;
                                                         break label800;
                                                      case 42:
                                                         b0 = 39;
                                                      default:
                                                         break label800;
                                                   }

                                                   b1 = b5;
                                                }

                                                if (b0 != -1) {
                                                   sprites[b0] = sprite1;
                                                }

                                                if (b1 != -1) {
                                                   imageSets[b1] = sprite1.moduleImages[0];
                                                }

                                                sprite1.moduleData = null;
                                                return;
                                             }

                                             gamecanvas2.genPackSkipBytes += k3;
                                             return;
                                          } catch (IOException ioexception) {
                                             return;
                                          }
                                       }
                                    } catch (Exception exception14) {
                                       return;
                                    }

                                    try {
                                       i -= 43;
                                       if (i >= 0 && i < 8) {
                                          int j = i;
                                          GameCanvas gamecanvas1 = this;

                                          try {
                                             if (j == 0) {
                                                gamecanvas1.stagePackStream.close();
                                                gamecanvas1.stagePackStream = null;
                                                System.gc();
                                                gamecanvas1.stagePackStream = gamecanvas1.getClass().getResourceAsStream("/cm.f");
                                                gamecanvas1.genPackEntryCount = gamecanvas1.stagePackStream.read();
                                                genPackDirectory = new byte[gamecanvas1.genPackEntryCount << 3];
                                                gamecanvas1.stagePackStream.read(genPackDirectory);
                                             }

                                             Sprite sprite;
                                             label828: {
                                                Object object2;
                                                byte b11;
                                                Object object3;
                                                label827: {
                                                   byte[] abyte = new byte[readInt(genPackDirectory, (j << 3) + 4)];
                                                   gamecanvas1.stagePackStream.read(abyte);
                                                   (sprite = new Sprite()).load(abyte, 0);
                                                   sprite.buildModuleImages(0, 0, -1, -1);
                                                   switch (j) {
                                                      case 0:
                                                         object2 = imageSets;
                                                         b11 = 11;
                                                         break;
                                                      case 1:
                                                         switch (gamecanvas1.world) {
                                                            case 1:
                                                            case 2:
                                                               if (gamecanvas1.world != 0) {
                                                                  sprite.buildModuleImages(gamecanvas1.world, 0, -1, -1);
                                                                  sprite.freeFirstPaletteImages(0);
                                                                  sprite.currentPalette = gamecanvas1.world;
                                                               }
                                                            default:
                                                               object2 = sprites;
                                                               b11 = 56;
                                                               object3 = sprite;
                                                               break label827;
                                                         }
                                                      case 2:
                                                         sprite.buildModuleImages(1, 0, 0, -1);
                                                         object2 = sprites;
                                                         b11 = 59;
                                                         object3 = sprite;
                                                         break label827;
                                                      case 3:
                                                         sprites[13] = sprite;
                                                         gamecanvas1.freeWaterImages();
                                                         break label828;
                                                      case 4:
                                                         object2 = imageSets;
                                                         b11 = 5;
                                                         break;
                                                      case 5:
                                                         object2 = imageSets;
                                                         b11 = 18;
                                                         break;
                                                      case 6:
                                                         sprites[43] = sprite;
                                                         object2 = imageSets;
                                                         b11 = 20;
                                                         object3 = null;
                                                         break label827;
                                                      default:
                                                         break label828;
                                                   }

                                                   object3 = sprite.moduleImages[0];
                                                }

                                                ((Object[])object2)[b11] = object3;
                                             }

                                             sprite.moduleData = null;
                                          } catch (Exception exception15) {
                                          }

                                          if (i == 7) {
                                             this.stagePackStream.close();
                                             this.stagePackStream = null;
                                             System.gc();
                                          }

                                          return;
                                       }
                                    } catch (Exception exception16) {
                                       return;
                                    }

                                    try {
                                       i -= 8;
                                       if (i >= 0 && i < 16) {
                                          if (i < cutscenes.length) {
                                             cutscenes[i] = new Cutscene(this);
                                             cutscenes[i].load(cutsceneIds[i]);
                                          }

                                          if (i == 15 && cutscenes.length >= 16) {
                                             for (int l2 = 16; l2 < cutscenes.length; l2++) {
                                                cutscenes[l2] = new Cutscene(this);
                                                cutscenes[l2].load(cutsceneIds[l2]);
                                             }
                                          }

                                          return;
                                       }
                                    } catch (Exception exception17) {
                                       return;
                                    }

                                    label926: {
                                       label927: {
                                          label878: {
                                             try {
                                                i -= 16;
                                                if (i >= 0 && i < 3) {
                                                   switch (this.stageKind) {
                                                      case 1:
                                                         this.loadMapSprite(i);
                                                         if (i == 2) {
                                                            this.stagePackStream.close();
                                                            this.stagePackStream = null;
                                                         }

                                                         return;
                                                      case 2:
                                                      default:
                                                         return;
                                                      case 3:
                                                         break label878;
                                                      case 4:
                                                         break label927;
                                                      case 5:
                                                         break label926;
                                                   }
                                                }
                                             } catch (Exception exception19) {
                                                return;
                                             }

                                             try {
                                                i -= 3;
                                                switch (i) {
                                                   case 0:
                                                      genPackDirectory = null;
                                                      this.loadObjectSprites(
                                                         sprites[12] != null
                                                            || imageSets[6] != null
                                                            || sprites[58] != null
                                                            || this.stageKind == 1
                                                            || this.stageKind == 4
                                                            || this.stageKind == 5
                                                      );
                                                      return;
                                                   case 1:
                                                      this.neededGenSprites = 0L;
                                                      this.neededExtraSprites = 0L;
                                                      this.rollerVariants = 0;
                                                      this.tipIndex++;
                                                      int k2 = this.tipIndex < 3 ? this.tipIndex : 3;
                                                      saveData[12] = (byte)k2;
                                                      this.writeSave();
                                                      return;
                                                   case 2:
                                                      checkpointObjectState = new int[this.mapWidth][this.mapHeight];
                                                      checkpointObjectTimers = new byte[this.mapWidth][this.mapHeight];
                                                      checkpointObjectMap = new byte[this.mapWidth][this.mapHeight];
                                                      checkpointTileMap = new int[this.mapWidth][this.mapHeight];
                                                      if (switchStates != null) {
                                                         checkpointSwitchStates = new byte[switchStates.length];
                                                         return;
                                                      }
                                                      break;
                                                   case 3:
                                                      GameCanvas gamecanvas = this;
                                                      if (this.hasEnemies) {
                                                         gamecanvas.resetEnemies();
                                                         tileSlots = new int[gamecanvas.mapWidth][gamecanvas.mapHeight];
                                                         gamecanvas.spawnEnemiesFromMap();
                                                      }

                                                      return;
                                                   case 4:
                                                      this.fadeHeight = 0;
                                                      this.drawnEffect = this.nextEffect = 0;
                                                      this.saveCheckpoint();
                                                      return;
                                                   case 5:
                                                      this.stageTitleUntil = stateTicks + 60;
                                                      this.invalidateHud();
                                                      return;
                                                   case 6:
                                                      unloadSprite(tipSprite, true);
                                                      tipSprite = null;
                                                      System.gc();
                                                      return;
                                                   case 7:
                                                      this.creatingNewGame = false;
                                                      mapFileData = null;
                                                      System.gc();
                                                      this.sound.play(16 + this.world);
                                                      gameState = 1;
                                                }

                                                return;
                                             } catch (Exception exception18) {
                                                return;
                                             }
                                          }

                                          if (i > 0) {
                                             return;
                                          }

                                          try {
                                             Sprite sprite4 = loadSpriteDefaultPalette("/mmv.f", 1, 0);
                                             imageSets[31] = sprite4.moduleImages[0];
                                             spriteInstances[5] = new SpriteInstance(loadSprite("/mm1.f", 0), 0, 0, null);
                                             spriteInstances[5].setAnim(0);
                                             return;
                                          } catch (Exception exception9) {
                                             return;
                                          }
                                       }

                                       if (i >= 2) {
                                          return;
                                       }

                                       try {
                                          this.loadBackgroundSprite(i);
                                          Sprite sprite3 = loadSpriteDefaultPalette("/mmv.f", 3, 0);
                                          imageSets[32] = sprite3.moduleImages[0];
                                          sprites[20] = loadSpriteDefaultPalette("/gen0.f", 7, 0);
                                          if (i == 1) {
                                             this.stagePackStream.close();
                                             this.stagePackStream = null;
                                          }

                                          return;
                                       } catch (Exception exception8) {
                                          return;
                                       }
                                    }

                                    try {
                                       Sprite sprite2 = loadSpriteDefaultPalette("/mmv.f", 2, 0);
                                       imageSets[30] = sprite2.moduleImages[0];
                                       sprites[20] = loadSpriteDefaultPalette("/gen0.f", 7, 0);
                                       spriteInstances[5] = new SpriteInstance(loadSprite("/b1.f", 0), 0, 0, null);
                                       spriteInstances[5].setAnim(10);
                                       return;
                                    } catch (Exception exception7) {
                                       return;
                                    }
                                 }

                                 try {
                                    label783: {
                                       byte b4;
                                       GameCanvas gamecanvas8;
                                       byte b7;
                                       if ((b4 = saveData[12]) < 1) {
                                          gamecanvas8 = this;
                                          b7 = b4;
                                       } else {
                                          if ((this.tipIndex + 8) % 8 >= 1) {
                                             break label783;
                                          }

                                          gamecanvas8 = this;
                                          b7 = 1;
                                       }

                                       gamecanvas8.tipIndex = b7;
                                    }

                                    loadTip(this.tipIndex % 8);
                                    return;
                                 } catch (Exception exception12) {
                                    return;
                                 }
                              }

                              try {
                                 backgroundBufferWidth = 360;
                                 backgroundBufferHeight = 216;
                                 backgroundBufferGraphics = (backgroundBuffer = Image.createImage(backgroundBufferWidth, backgroundBufferHeight)).getGraphics();
                                 GameCanvas gamecanvas5 = this;
                                 bufferedLeft = -1;
                                 pickupAnimStarting = false;
                                 gamecanvas5.playerVisible = true;
                                 gamecanvas5.nextCompassTarget = 0;
                                 gamecanvas5.screenShakeTicks = 0;
                                 gamecanvas5.neededGenSprites = 0L;
                                 gamecanvas5.neededExtraSprites = 0L;
                                 gamecanvas5.padlockX = 0;
                                 gamecanvas5.padlockY = 0;
                                 gamecanvas5.stageKind = 0;
                                 gamecanvas5.stageCompletedBefore = (gamecanvas5.getStageFlags(gamecanvas5.world, gamecanvas5.stage) & 2) != 0;
                                 gamecanvas5.switchRevealPhase = 0;
                                 switch (gamecanvas5.world) {
                                    case 0:
                                       if (gamecanvas5.stage == 5) {
                                          gamecanvas5.stageKind = 1;
                                          gamecanvas5.floodHeight = 816;
                                          gamecanvas5.floodSwitchPresses = 0;
                                       } else if (gamecanvas5.stage == 13) {
                                          gamecanvas5.stageKind = 2;
                                          gamecanvas5.prologueMarkerHint = false;
                                       } else if (gamecanvas5.stage == 8) {
                                          gamecanvas5.stageKind = 4;
                                          gamecanvas5.bossAnim = 0;
                                          gamecanvas5.bossHealth = 3;
                                          gamecanvas5.bossPosition = 0;
                                          gamecanvas5.bossTimer = 0;
                                          gamecanvas5.bossSlamVisible = false;
                                          gamecanvas5.neededGenSprites |= 8L;
                                          gamecanvas5.neededGenSprites |= 1024L;
                                          gamecanvas5.arenaWallRow = 2;
                                          gamecanvas5.arenaWallX1 = 12;
                                          gamecanvas5.arenaWallX2 = 15;
                                          gamecanvas5.arenaWallRow2 = 5;
                                       }
                                       break;
                                    case 1:
                                       if (gamecanvas5.stage == 9) {
                                          gamecanvas5.initBossFight();
                                       }
                                       break;
                                    case 2:
                                       if (gamecanvas5.stage == 10) {
                                          gamecanvas5.resetBoss();
                                          gamecanvas5.yetiX = 360;
                                          gamecanvas5.stageKind = 3;
                                          gamecanvas5.neededGenSprites |= 128L;
                                          gamecanvas5.neededGenSprites |= 8L;
                                       }
                                 }

                                 gamecanvas5.prologueSaw13 = false;
                                 gamecanvas5.prologueSaw16 = false;
                                 gamecanvas5.prologueMarkerReached = false;
                                 gamecanvas5.padlockGemsRemaining = 0;
                                 gamecanvas5.hookedObjectState = -1;
                                 gamecanvas5.hookRestoreState = -1;
                                 gamecanvas5.hookPullDelay = 0;
                                 gamecanvas5.hookX = 0;
                                 gamecanvas5.hookY = 0;
                                 gamecanvas5.hookPullSteps = 0;
                                 gamecanvas5.idleAnimDelay = 70;
                                 gamecanvas5.energy = saveData[8];
                                 gamecanvas5.queuedAction = 0;
                                 gamecanvas5.stageDiamonds = 0;
                                 gamecanvas5.stageHits = 0;
                                 gamecanvas5.stageRetries = 0;
                                 gamecanvas5.stageRedDiamonds = 0;
                                 gamecanvas5.deathTimer = 0;
                                 gamecanvas5.playerFlags = 0;
                                 gamecanvas5.escapePressesLeft = 0;
                                 gamecanvas5.goldKeys = 0;
                                 gamecanvas5.silverKeys = 0;
                                 activeSwitchId = -1;
                                 switchStates = null;
                                 switchTargetObjects = null;
                                 gamecanvas5.stagePackStream = gamecanvas5.getClass().getResourceAsStream(stagePackFiles[gamecanvas5.world]);
                                 gamecanvas5.stagePackStream.read();
                                 boolean flag = false;

                                 while (!flag) {
                                    int l3 = gamecanvas5.stagePackStream.read();
                                    int j4 = 0;
                                    byte[] abyte1 = new byte[4];

                                    while (j4 < l3 && !flag) {
                                       gamecanvas5.stagePackStream.read(abyte1);
                                       int j2 = readShort(abyte1, 0);
                                       int l4 = readShort(abyte1, 2);
                                       if (j4 != gamecanvas5.stage) {
                                          gamecanvas5.stagePackStream.skip(j2 * l4 * 3);
                                       } else {
                                          gamecanvas5.mapWidth = j2;
                                          gamecanvas5.mapHeight = l4;
                                          objectMap = null;
                                          tileRedrawTimers = null;
                                          tileMap = null;
                                          System.gc();
                                          objectMap = new byte[gamecanvas5.mapWidth][gamecanvas5.mapHeight];
                                          tileRedrawTimers = new byte[gamecanvas5.mapWidth][gamecanvas5.mapHeight];
                                          tileMap = new int[gamecanvas5.mapWidth][gamecanvas5.mapHeight];
                                          abyte1 = new byte[gamecanvas5.mapWidth * gamecanvas5.mapHeight];
                                          gamecanvas5.stagePackStream.read(abyte1);

                                          for (int j5 = 0; j5 < gamecanvas5.mapWidth; j5++) {
                                             for (int k6 = 0; k6 < gamecanvas5.mapHeight; k6++) {
                                                objectMap[j5][k6] = abyte1[j5 + k6 * gamecanvas5.mapWidth];
                                             }
                                          }

                                          objectTimers = new byte[gamecanvas5.mapWidth][gamecanvas5.mapHeight];
                                          objectState = new int[gamecanvas5.mapWidth][gamecanvas5.mapHeight];
                                          gamecanvas5.stagePackStream.read(abyte1);

                                          for (int k5 = 0; k5 < gamecanvas5.mapWidth; k5++) {
                                             for (int l6 = 0; l6 < gamecanvas5.mapHeight; l6++) {
                                                objectState[k5][l6] = abyte1[k5 + l6 * gamecanvas5.mapWidth];
                                             }
                                          }

                                          gamecanvas5.stagePackStream.read(abyte1);

                                          for (int l5 = 0; l5 < gamecanvas5.mapWidth; l5++) {
                                             for (int i7 = 0; i7 < gamecanvas5.mapHeight; i7++) {
                                                tileMap[l5][i7] = abyte1[l5 + i7 * gamecanvas5.mapWidth];
                                             }
                                          }

                                          abyte1 = null;
                                          flag = true;
                                       }

                                       j4++;
                                    }
                                 }

                                 gamecanvas5.stagePackStream.close();
                                 gamecanvas5.stagePackStream = null;
                                 gamecanvas5.cameraX = gamecanvas5.cameraTargetX = 0;
                                 gamecanvas5.cameraY = gamecanvas5.cameraTargetY = 0;
                                 gamecanvas5.updateCamera();
                                 System.gc();
                                 return;
                              } catch (Exception exception11) {
                                 return;
                              }
                           }

                           try {
                              GameCanvas gamecanvas4 = this;
                              Hashtable hashtable1 = new Hashtable();
                              Hashtable hashtable = new Hashtable();
                              Hashtable hashtable2 = new Hashtable();
                              gamecanvas4.takenTreasureCount = 0;
                              gamecanvas4.checkpointTreasureCount = 0;
                              int i4 = 0;
                              gamecanvas4.stageDiamondTotal = 0;
                              gamecanvas4.rollerVariants = 0;
                              int i2 = 0;
                              int k4 = 0;
                              gamecanvas4.stageHasDoors9 = false;
                              gamecanvas4.stageHasDoors8 = false;
                              gamecanvas4.stageHasWindPods = false;
                              gamecanvas4.collapsingRowCount = 0;
                              int i5 = 0;

                              for (int k = 0; k < gamecanvas4.mapWidth; k++) {
                                 for (int l = 0; l < gamecanvas4.mapHeight; l++) {
                                    objectTimers[k][l] = 0;
                                    tileRedrawTimers[k][l] = 0;
                                    int i1 = tileMap[k][l];
                                    int j1 = objectState[k][l];
                                    byte b2 = objectMap[k][l];
                                    if (i1 != -1) {
                                       label725:
                                       switch (i1 & 0xFF) {
                                          case 0:
                                             i5++;
                                             tileMap[k][l] = j1 << 8 | i1 & 0xFF;
                                             break;
                                          case 2:
                                             gamecanvas4.neededGenSprites |= 1048576L;
                                             switch (j1) {
                                                case 0:
                                                case 1:
                                                   gamecanvas4.neededGenSprites |= 524288L;
                                                default:
                                                   tileMap[k][l] = j1 << 8 | 2;
                                                   break label725;
                                             }
                                          case 3:
                                             tileRedrawTimers[k][l] = 127;
                                             if (j1 > 0) {
                                                tileMap[k][l] = j1 + 1 << 8 | 3;
                                             }
                                             break;
                                          case 4:
                                             i2++;
                                             gamecanvas4.neededGenSprites |= 16L;
                                             tileMap[k][l] = j1 << 8 | i1 & 0xFF;
                                             break;
                                          case 5:
                                             gamecanvas4.exitDirectionA = (byte)j1;
                                             break;
                                          case 6:
                                             Integer integer6 = new Integer(j1);
                                             Integer integer9;
                                             integer9 = (integer9 = (Integer)hashtable1.get(integer6)) == null ? new Integer(1) : new Integer(integer9 + 1);
                                             hashtable1.put(integer6, integer9);
                                             gamecanvas4.neededGenSprites |= 536870912L;
                                             tileMap[k][l] = j1 << 8 | 6;
                                             break;
                                          case 7:
                                             if (j1 != -1) {
                                                doorX[j1] = (byte)k;
                                                doorY[j1] = (byte)l;
                                             }

                                             tileMap[k][l] = j1 << 8 | i1;
                                             break;
                                          case 8:
                                             gamecanvas4.stageHasDoors8 = true;
                                          case 9:
                                             if ((i1 & 0xFF) != 8) {
                                                gamecanvas4.stageHasDoors9 = true;
                                             }

                                             gamecanvas4.neededGenSprites |= 268435456L;
                                             Integer integer5 = new Integer(j1);
                                             Integer integer8;
                                             integer8 = (integer8 = (Integer)hashtable1.get(integer5)) == null ? new Integer(1) : new Integer(integer8 + 1);
                                             hashtable1.put(integer5, integer8);
                                             tileMap[k][l] = j1 << 8 | i1;
                                             break;
                                          case 10:
                                          case 11:
                                          case 12:
                                          case 13:
                                          case 15:
                                          case 16:
                                          case 18:
                                          case 20:
                                          case 21:
                                          case 22:
                                          case 23:
                                          case 24:
                                          case 25:
                                          case 27:
                                          case 29:
                                          case 32:
                                          default:
                                             if (i1 >= 20 && i1 < 26) {
                                                tileMap[k][l] = i1;
                                                GameCanvas gamecanvas7;
                                                long k9;
                                                long k10;
                                                switch (gamecanvas4.world) {
                                                   case 0:
                                                      gamecanvas7 = gamecanvas4;
                                                      k9 = gamecanvas4.neededGenSprites;
                                                      k10 = 16L;
                                                      break;
                                                   case 1:
                                                      gamecanvas7 = gamecanvas4;
                                                      k9 = gamecanvas4.neededGenSprites;
                                                      k10 = 2097152L;
                                                      break;
                                                   default:
                                                      break label725;
                                                }

                                                gamecanvas7.neededGenSprites = k9 | k10;
                                             } else if (i1 < 80 && i1 > -1) {
                                                tileMap[k][l] = -1;
                                             }
                                             break;
                                          case 14:
                                          case 33:
                                             if (!gamecanvas4.isBossStage()) {
                                                tileMap[k][l] = 33;
                                             }

                                             gamecanvas4.neededGenSprites = gamecanvas4.neededGenSprites | 1L << ((tileMap[k][l] & 0xFF) == 14 ? 22 : 33);
                                             if (gamecanvas4.isTreasureAvailable(gamecanvas4.world, gamecanvas4.stage, k, l)) {
                                                if (gamecanvas4.isBossStage()) {
                                                   objectMap[k][l] = 41;
                                                   objectState[k][l] = 10;
                                                   gamecanvas4.stageDiamondTotal += 10;
                                                } else {
                                                   objectMap[k][l] = -1;
                                                   tileMap[k][l] = tileMap[k][l] | 256;
                                                }
                                             }
                                             break;
                                          case 17:
                                             int k1;
                                             if ((k1 = tileMap[k][l - 1] & 0xFF) == 14 || k1 == 33) {
                                                tileMap[k][l - 1] = 0xFF00 | k1;
                                             }

                                             byte b3;
                                             switch (b3 = objectMap[k][l - 1]) {
                                                case 19:
                                                case 36:
                                                case 43:
                                                case 45:
                                                case 46:
                                                case 49:
                                                   Integer integer = new Integer(j1);
                                                   Integer integer1;
                                                   integer1 = (integer1 = (Integer)hashtable.get(integer)) == null ? new Integer(1) : new Integer(integer1 + 1);
                                                   hashtable.put(integer, integer1);
                                                   tileMap[k][l] = -1;
                                                   Integer integer2 = (Integer)hashtable2.get(integer);
                                                   if (b3 == 36) {
                                                      if (integer2 == null) {
                                                         hashtable2.put(integer, new Integer(58));
                                                      }
                                                   } else {
                                                      hashtable2.put(integer, new Integer(56));
                                                      if (gamecanvas4.stageKind == 4) {
                                                         hashtable2.put(integer, new Integer(51));
                                                      } else if (gamecanvas4.stageKind == 5) {
                                                         hashtable2.put(integer, new Integer(52));
                                                      } else if (gamecanvas4.stageKind == 3) {
                                                         hashtable2.put(integer, new Integer(53));
                                                      }
                                                   }
                                                   break;
                                                default:
                                                   tileMap[k][l] = j1 << 8 | 17;
                                             }

                                             if (j1 >= 0) {
                                                k4 |= 1 << j1;
                                             }
                                             break;
                                          case 28:
                                             gamecanvas4.exitDirectionB = (byte)j1;
                                             break;
                                          case 30:
                                             gamecanvas4.neededGenSprites |= 1073741824L;
                                             i5++;
                                          case 1:
                                          case 26:
                                             tileMap[k][l] = j1 << 8 | i1 & 0xFF;
                                             break;
                                          case 31:
                                             gamecanvas4.neededGenSprites |= 1073741824L;
                                             tileMap[k][l] = j1 << 8 | 31;
                                          case 19:
                                             tileMap[k][l] = j1 << 8 | 19;
                                             break;
                                          case 34:
                                             gamecanvas4.neededGenSprites |= 17179869184L;
                                       }
                                    }

                                    byte[] abyte4;
                                    int l9;
                                    byte b8;
                                    switch (b2) {
                                       case 1:
                                          gamecanvas4.stageDiamondTotal++;
                                          tileRedrawTimers[k][l] = 48;
                                          objectState[k][l] = 0;
                                          continue;
                                       case 3:
                                       case 9:
                                       case 13:
                                       case 15:
                                       case 17:
                                       case 20:
                                       case 21:
                                       case 25:
                                       case 29:
                                       case 32:
                                       case 50:
                                       case 54:
                                       case 55:
                                       case 56:
                                       case 57:
                                       case 58:
                                       case 59:
                                       case 60:
                                       case 61:
                                       case 62:
                                       case 63:
                                       case 64:
                                       case 65:
                                       case 66:
                                       case 67:
                                       case 68:
                                       case 69:
                                       case 70:
                                       case 71:
                                       case 72:
                                       case 73:
                                       case 74:
                                       case 75:
                                       case 76:
                                       case 77:
                                       case 78:
                                       default:
                                          if (b2 >= 80 || b2 <= -1) {
                                             continue;
                                          }

                                          abyte4 = objectMap[k];
                                          l9 = l;
                                          b8 = -1;
                                          break;
                                       case 6:
                                          i4++;
                                          gamecanvas4.placeItemChest(k, l);
                                       case 7:
                                          gamecanvas4.neededGenSprites |= 16L;
                                          objectState[k][l] = 0;
                                          continue;
                                       case 8:
                                          gamecanvas4.neededGenSprites |= 32L;
                                          gamecanvas4.neededGenSprites |= 8L;
                                       case 4:
                                          if (b2 != 8) {
                                             gamecanvas4.placeItemChest(k, l);
                                          }
                                       case 5:
                                          gamecanvas4.neededGenSprites |= 4L;
                                       case 2:
                                          i4++;
                                       case 0:
                                          tileRedrawTimers[k][l] = 48;
                                          objectState[k][l] = 0;
                                          gamecanvas4.neededExtraSprites |= 1L;
                                          continue;
                                       case 10:
                                          objectState[k][l] = 0;
                                          gamecanvas4.neededExtraSprites |= 2L;
                                          continue;
                                       case 11:
                                          objectState[k][l] = j1 == 1 ? 16 : 0;
                                          tileRedrawTimers[k][l] = 48;
                                          gamecanvas4.neededGenSprites |= 16384L;
                                          continue;
                                       case 12:
                                          objectMap[k][l] = -1;
                                          gamecanvas4.padlockX = k;
                                          gamecanvas4.padlockY = l;
                                          gamecanvas4.padlockGemsRemaining = j1;
                                          continue;
                                       case 14:
                                          gamecanvas4.neededGenSprites |= 4096L;
                                          objectState[k][l] = objectState[k][l] == 4 ? 8 : 0;
                                          abyte4 = tileRedrawTimers[k];
                                          l9 = l;
                                          b8 = 24;
                                          break;
                                       case 16:
                                          if (objectMap[k][l + 1] != 16) {
                                             objectMap[k][l - 1] = 16;
                                             objectState[k][l - 1] = j1;
                                          }

                                          gamecanvas4.neededGenSprites |= 8192L;
                                          continue;
                                       case 18:
                                          gamecanvas4.togglePhase = 0;
                                          gamecanvas4.toggleDirection = 0;
                                          gamecanvas4.neededGenSprites |= 549755813888L;
                                          gamecanvas4.neededGenSprites |= 128L;
                                          continue;
                                       case 19:
                                          gamecanvas4.neededGenSprites = gamecanvas4.neededGenSprites | 1L << (gamecanvas4.world == 1 ? 17 : 15);
                                          gamecanvas4.rollerVariants |= 2;
                                          abyte4 = tileRedrawTimers[k];
                                          l9 = l;
                                          b8 = 48;
                                          break;
                                       case 22:
                                       case 23:
                                          gamecanvas4.neededGenSprites |= 512L;
                                          gamecanvas4.neededGenSprites |= 1024L;
                                          abyte4 = tileRedrawTimers[k];
                                          l9 = l;
                                          b8 = 48;
                                          break;
                                       case 26:
                                          gamecanvas4.neededGenSprites |= 1099511627776L;
                                       case 24:
                                       case 27:
                                          i5++;
                                          i4++;
                                          gamecanvas4.neededGenSprites |= 1073741824L;
                                          gamecanvas4.neededGenSprites |= 524288L;
                                          gamecanvas4.placeItemChest(k, l);
                                          continue;
                                       case 28:
                                          gamecanvas4.neededGenSprites |= 2048L;
                                          if (j1 > 10) {
                                             objectState[k][l] = objectState[k][l] / 11;
                                             objectState[k][l] = objectState[k][l] | 8;
                                          }

                                          abyte4 = tileRedrawTimers[k];
                                          l9 = l;
                                          b8 = 24;
                                          break;
                                       case 30:
                                          gamecanvas4.neededGenSprites |= 128L;
                                          objectState[k][l] = 0;
                                       case 31:
                                       case 33:
                                       case 51:
                                       case 52:
                                       case 53:
                                          continue;
                                       case 34:
                                          objectMap[k][l] = -1;
                                          tileMap[k][l] = 15;
                                          gamecanvas4.neededGenSprites |= 16777216L;
                                          continue;
                                       case 35:
                                          objectMap[k][l] = 35;
                                          tileMap[k][l] = -1;
                                          gamecanvas4.neededGenSprites |= 16777216L;
                                          gamecanvas4.stageHasWindPods = true;
                                          continue;
                                       case 36:
                                          if (objectState[k][l] != 1) {
                                             objectState[k][l] = 0;
                                          }

                                          gamecanvas4.neededGenSprites |= 256L;
                                          continue;
                                       case 37:
                                          gamecanvas4.neededGenSprites |= 33554432L;
                                          objectState[k][l] = 0;
                                          continue;
                                       case 38:
                                          gamecanvas4.hasEnemies = true;
                                          gamecanvas4.neededGenSprites |= 67108864L;
                                          tileMap[k][l] = 27;
                                          gamecanvas4.neededGenSprites |= 64L;
                                          continue;
                                       case 39:
                                          gamecanvas4.hasEnemies = true;
                                          gamecanvas4.neededGenSprites |= 67108864L;
                                          continue;
                                       case 40:
                                          gamecanvas4.neededGenSprites |= 1073741824L;
                                          i5++;
                                          gamecanvas4.hasEnemies = true;
                                          gamecanvas4.neededGenSprites |= 134217728L;
                                          gamecanvas4.placeItemChest(k, l);
                                          i4++;
                                          continue;
                                       case 41:
                                          if (objectState[k][l] <= 0) {
                                             objectState[k][l] = 1;
                                          }

                                          gamecanvas4.stageDiamondTotal = gamecanvas4.stageDiamondTotal + objectState[k][l];
                                          continue;
                                       case 42:
                                          i5++;
                                          i4++;
                                          gamecanvas4.neededGenSprites |= 2147483648L;
                                          gamecanvas4.neededGenSprites |= 1073741824L;
                                          gamecanvas4.placeItemChest(k, l);
                                          continue;
                                       case 43:
                                          gamecanvas4.neededGenSprites = gamecanvas4.neededGenSprites | 1L << (gamecanvas4.world == 1 ? 17 : 15);
                                          gamecanvas4.rollerVariants |= 1;
                                          objectState[k][l] = j1 & -98305 | 65536;
                                          abyte4 = tileRedrawTimers[k];
                                          l9 = l;
                                          b8 = 48;
                                          break;
                                       case 44:
                                          tileRedrawTimers[k][l] = 24;
                                          objectState[k][l] = 0;
                                          gamecanvas4.neededGenSprites |= 17179869184L;
                                          continue;
                                       case 45:
                                          objectState[k][l] = 0;
                                          tileRedrawTimers[k][l] = 24;
                                          gamecanvas4.neededGenSprites |= 34359738368L;
                                          continue;
                                       case 46:
                                          objectState[k][l] = 0;
                                          tileRedrawTimers[k][l] = 24;
                                          objectTimers[k][l] = 0;
                                          gamecanvas4.neededGenSprites |= 137438953472L;
                                          continue;
                                       case 47:
                                          tileRedrawTimers[k][l] = 48;
                                          objectState[k][l] = 0;
                                          gamecanvas4.neededGenSprites |= 8388608L;
                                          continue;
                                       case 48:
                                          objectState[k][l] = (j1 & 7) == 4 ? 16 : 0;
                                          gamecanvas4.collapsingRowCount++;
                                          gamecanvas4.neededGenSprites |= 1099511627776L;
                                          gamecanvas4.neededGenSprites |= 4294967296L;
                                          int j8 = l - 1;
                                          objectMap[k][j8] = 48;
                                          objectState[k][j8] = 8;
                                          markAreaDirty(k, j8);
                                          continue;
                                       case 49:
                                          gamecanvas4.neededGenSprites |= 2199023255552L;
                                          abyte4 = tileRedrawTimers[k];
                                          l9 = l;
                                          b8 = 48;
                                          break;
                                       case 79:
                                          gamecanvas4.playerX = 0;
                                          gamecanvas4.playerY = l;
                                          gamecanvas4.entryWalkSteps = k;
                                          objectMap[k][l] = -1;
                                          gamecanvas4.cameraX = gamecanvas4.cameraTargetX = 0;
                                          gamecanvas4.cameraY = gamecanvas4.cameraTargetY = gamecanvas4.playerY * 24 - 120;
                                          continue;
                                    }

                                    abyte4[l9] = b8;
                                 }
                              }

                              takenTreasures = new byte[i4 << 1];

                              for (int i6 = 0; i6 < takenTreasures.length; i6++) {
                                 takenTreasures[i6] = 0;
                              }

                              cutscenes = new Cutscene[i5];
                              cutsceneIds = new byte[i5];
                              int j6 = 0;
                              compassTargets = new byte[i2 + 1 << 1];
                              int j7 = 31;

                              while (j7 >= 0 && (k4 & 1 << j7) == 0) {
                                 j7--;
                              }

                              if (++j7 > 0) {
                                 switchStates = new byte[j7];
                                 switchTargetObjects = new byte[j7];
                              }

                              if (gamecanvas4.collapsingRowCount > 0) {
                                 collapsingRows = new byte[gamecanvas4.collapsingRowCount * 3];
                              }

                              int k7 = 0;

                              for (int l7 = 0; l7 < gamecanvas4.mapHeight; l7++) {
                                 for (int i8 = 0; i8 < gamecanvas4.mapWidth; i8++) {
                                    label676: {
                                       int k8 = tileMap[i8][l7] & 0xFF;
                                       int l8 = tileMap[i8][l7] >> 8;
                                       byte[] abyte5;
                                       int i10;
                                       byte b9;
                                       switch (k8) {
                                          case 0:
                                          case 30:
                                             abyte5 = cutsceneIds;
                                             i10 = j6++;
                                             b9 = (byte)l8;
                                             break;
                                          case 4:
                                             i4 = l8;
                                             compassTargets[i4 << 1] = (byte)i8;
                                             abyte5 = compassTargets;
                                             i10 = (i4 << 1) + 1;
                                             b9 = (byte)l7;
                                             break;
                                          case 5:
                                             compassTargets[i2 << 1] = (byte)i8;
                                             abyte5 = compassTargets;
                                             i10 = (i2 << 1) + 1;
                                             b9 = (byte)l7;
                                             break;
                                          case 7:
                                             Integer integer7 = (Integer)hashtable1.get(new Integer(l8));
                                             l8 <<= 8;
                                             if (integer7 != null) {
                                                l8 = l8 & -16 | integer7;
                                             }

                                             boolean flag1 = (tileMap[i8][l7 - 1] & 0xFF) == 17;
                                             boolean flag2 = (tileMap[i8][l7 + 1] & 0xFF) == 17
                                                && (tileMap[i8 + 1][l7] & 0xFF) != 26
                                                && (tileMap[i8 - 1][l7] & 0xFF) != 26;
                                             if (flag1 || flag2) {
                                                l8 = l8 & -241 | 48;
                                                tileRedrawTimers[i8][l7] = 24;
                                                if (flag1) {
                                                   tileMap[i8][l7 - 1] = -1;
                                                }
                                             }

                                             tileMap[i8][l7] = l8 << 8 | k8;
                                             break label676;
                                          case 17:
                                             if (l8 != -1) {
                                                Integer integer3 = new Integer(l8);
                                                Integer integer4;
                                                if ((integer4 = (Integer)hashtable.get(integer3)) == null) {
                                                   integer4 = new Integer(0);
                                                }

                                                switchStates[l8] = integer4.byteValue();
                                                if ((integer3 = (Integer)hashtable2.get(integer3)) == null) {
                                                   integer3 = new Integer(57);
                                                }

                                                abyte5 = switchTargetObjects;
                                                i10 = l8;
                                                b9 = integer3.byteValue();
                                                break;
                                             }
                                          default:
                                             break label676;
                                       }

                                       abyte5[i10] = b9;
                                    }

                                    byte[] abyte6;
                                    int j10;
                                    byte b10;
                                    switch (objectMap[i8][l7]) {
                                       case 24:
                                          abyte6 = cutsceneIds;
                                          j10 = j6++;
                                          b10 = 22;
                                          break;
                                       case 26:
                                          abyte6 = cutsceneIds;
                                          j10 = j6++;
                                          b10 = 25;
                                          break;
                                       case 27:
                                          abyte6 = cutsceneIds;
                                          j10 = j6++;
                                          b10 = 23;
                                          break;
                                       case 40:
                                          abyte6 = cutsceneIds;
                                          j10 = j6++;
                                          b10 = 24;
                                          break;
                                       case 42:
                                          abyte6 = cutsceneIds;
                                          j10 = j6++;
                                          b10 = 11;
                                          break;
                                       case 48:
                                          if ((objectState[i8][l7] & 8) != 0) {
                                             int i9 = i8 + ((objectState[i8][l7 + 1] & 16) == 0 ? 1 : -1);
                                             int j9 = k7 * 3;
                                             if (fallingVariant(i9, l7) >= 0) {
                                                collapsingRows[j9] = (byte)i9;
                                                collapsingRows[j9 + 1] = (byte)i9;
                                                abyte6 = collapsingRows;
                                                j10 = j9 + 2;
                                                b10 = (byte)l7;
                                             } else {
                                                abyte6 = collapsingRows;
                                                j10 = j9 + 2;
                                                b10 = -1;
                                             }

                                             abyte6[j10] = b10;
                                             objectState[i8][l7] = objectState[i8][l7] & 16777215 | k7 << 24;
                                             k7++;
                                          }
                                       default:
                                          continue;
                                    }

                                    abyte6[j10] = b10;
                                 }
                              }

                              tileMap[gamecanvas4.entryWalkSteps - 2][gamecanvas4.playerY] = -193 << 8 | 7;
                              if ((gamecanvas4.neededGenSprites & 4294967296L) != 0L || saveData[9] >= 8) {
                                 gamecanvas4.neededGenSprites |= 1099511627776L;
                                 gamecanvas4.neededGenSprites |= 2L;
                                 gamecanvas4.neededGenSprites |= 1L;
                                 if ((gamecanvas4.neededGenSprites & 131072L) != 0L) {
                                    gamecanvas4.neededGenSprites |= 262144L;
                                 }

                                 if ((gamecanvas4.neededGenSprites & 32768L) != 0L) {
                                    gamecanvas4.neededGenSprites |= 65536L;
                                 }

                                 if ((gamecanvas4.neededGenSprites & 34359738368L) != 0L) {
                                    gamecanvas4.neededGenSprites |= 68719476736L;
                                 }

                                 if ((gamecanvas4.neededGenSprites & 137438953472L) != 0L) {
                                    gamecanvas4.neededGenSprites |= 274877906944L;
                                 }

                                 if ((gamecanvas4.neededGenSprites & 2199023255552L) != 0L) {
                                    gamecanvas4.neededGenSprites |= 4398046511104L;
                                 }
                              }

                              return;
                           } catch (Exception exception10) {
                              return;
                           }
                        }

                        try {
                           if (this.reloadLivesOnLoad) {
                              this.loadLives();
                           }

                           return;
                        } catch (Exception exception6) {
                           return;
                        }
                     }

                     try {
                        if (this.gcOnLoad) {
                           System.gc();
                           this.gcOnLoad = false;
                        }

                        return;
                     } catch (Exception exception5) {
                        return;
                     }
                  }

                  try {
                     FontRenderer.resetColor();
                     FontRenderer.resetColor();
                     return;
                  } catch (Exception exception4) {
                     return;
                  }
               }

               try {
                  if (this.freeStageOnLoad) {
                     this.freeStageResources();
                  }

                  return;
               } catch (Exception exception3) {
                  return;
               }
            }

            try {
               if (this.unloadMapOnLoad) {
                  this.unloadMapResources();
               }

               return;
            } catch (Exception exception2) {
               return;
            }
         }

         try {
            GameCanvas gamecanvas6 = this;
            this.stageRedDiamondTotal = gamecanvas6.getStageDiamondTotal(gamecanvas6.world, gamecanvas6.stage);
            return;
         } catch (Exception exception1) {
            return;
         }
      }

      try {
         String s = "/" + this.world + ".f";
         this.stagePackStream = this.getClass().getResourceAsStream(s);
         this.genPackEntryCount = this.stagePackStream.read();
         genPackDirectory = new byte[this.genPackEntryCount << 3];
         this.stagePackStream.read(genPackDirectory);
      } catch (Exception exception) {
      }
   }

   private void loadObjectSprites(boolean var1) {
      try {
         InputStream inputstream;
         byte[] abyte = new byte[(inputstream = this.getClass().getResourceAsStream("/o.f")).read() << 3];
         inputstream.read(abyte);
         byte[] abyte1 = new byte[readInt(abyte, 4)];
         inputstream.read(abyte1);
         if (!var1) {
            inputstream.close();
            inputstream = null;
            System.gc();
         }

         Sprite sprite;
         (sprite = new Sprite()).load(abyte1, 0);
         this.armorPalette = saveData[8] - 4;
         sprite.buildModuleImages(this.armorPalette, 0, -1, -1);
         sprite.currentPalette = this.armorPalette;
         sprite.moduleData = null;
         spriteInstances[0] = new SpriteInstance(sprite, 0, 0, null);
         System.gc();
         if (var1) {
            abyte1 = new byte[readInt(abyte, 12)];
            inputstream.read(abyte1);
            inputstream.close();
            System.gc();
            (sprite = new Sprite()).load(abyte1, 0);
            sprite.buildModuleImages(0, 0, -1, -1);
            sprite.moduleData = null;
            spriteInstances[3] = new SpriteInstance(sprite, 0, 0, null);
            System.gc();
         }
      } catch (Exception exception) {
      }
   }

   private int selectedMenuAction() {
      return menus[this.menu][this.menuSelection << 1];
   }

   private void openMoreGames() {
      if (moreGamesUrl != null) {
         String s = moreGamesUrl;

         try {
            Thread.sleep(10L);
            new PlatformRequestThread(s).start();
            return;
         } catch (Exception exception) {
         }
      }
   }

   private void startStage() {
      GameCanvas gamecanvas;
      byte b0;
      if (this.creatingNewGame) {
         gamecanvas = this;
         b0 = 97;
      } else {
         gamecanvas = this;
         b0 = 92;
      }

      gamecanvas.loadingStepCount = b0;
      this.loadingStep = 0;
      if (!hasStartedGame()) {
         this.hasCompass = false;
      }

      this.freeStageOnLoad = true;
      this.cheatSkipWalking = false;
      gameState = 11;
      this.playerFlags = this.queuedAction = this.playerAction = 0;
      this.pickupIconSet = this.pickupIconFrame = -1;
      this.invulnerableTicks = 0L;
   }

   private static boolean isMoreGamesEnabled() {
      String s = null;

      try {
         s = midlet.getAppProperty("more_games_status");
      } catch (Exception exception) {
      }

      return s != null ? s.equals("on") : false;
   }

   private void menuUp() {
      this.menuSelection--;
      if (this.menuSelection < 0) {
         this.menuSelection = (menus[this.menu].length >> 1) - 1;
      }
   }

   private void menuDown() {
      this.menuSelection = (this.menuSelection + 1) % (menus[this.menu].length >> 1);
   }

   private void unloadCutscenes() {
      this.unloadCutscenesOnLoad = false;
      if (cutscenes != null) {
         int j = cutscenes.length;

         for (int i = 0; i < j; i++) {
            unloadCutscene(cutscenes[i]);
            cutscenes[i] = null;
         }
      }

      cutscenes = null;
      unloadSprite(sprites[42], true);
      unloadSprite(sprites[43], true);
      System.gc();
   }

   private static void unloadCutscene(Cutscene var0) {
      if (var0 != null) {
         if (Cutscene.sprites != null) {
            for (int i = 0; i < Cutscene.sprites.length; i++) {
               unloadSprite(Cutscene.sprites[i], true);
               Cutscene.sprites[i] = null;
            }
         }

         if (var0.steps != null) {
            for (int j = 0; j < var0.steps.length; j++) {
               var0.steps[j] = null;
            }

            var0.steps = null;
         }

         var0.currentStep = null;
      }
   }

   private static void unloadSprite(Sprite var0, boolean var1) {
      if (var0 != null) {
         var0.unload(var1);
      }
   }

   private void freeStageResources() {
      this.freeStageOnLoad = false;
      this.hudDirty = true;
      this.cameraPanToCheckpoint = false;
      this.hintTextTicks = 0;
      this.hintText = null;
      switchStates = null;
      switchTargetObjects = null;
      this.takenTreasureCount = 0;
      this.checkpointTreasureCount = 0;
      takenTreasures = null;
      collapsingRows = null;
      this.stagePackStream = null;
      genPackDirectory = null;
      mapPreview = null;
      mapPreviewGraphics = null;
      backgroundBufferGraphics = null;
      backgroundBuffer = null;
      cutsceneIds = null;
      compassTargets = null;
      if (cutscenes != null) {
         int i = cutscenes.length;

         for (int j = 0; j < i; j++) {
            unloadCutscene(cutscenes[j]);
            cutscenes[j] = null;
         }
      }

      cutscenes = null;
      if (this.activeCutscene != null) {
         unloadCutscene(this.activeCutscene);
         this.activeCutscene = null;
      }

      if (imageSets != null) {
         for (int l = 0; l < 33; l++) {
            if (imageSets[l] != null) {
               int k1 = imageSets[l].length;

               for (int k = 0; k < k1; k++) {
                  imageSets[l][k] = null;
               }

               imageSets[l] = null;
            }
         }
      }

      unloadSprite(slotSprite, true);
      System.gc();
      if (sprites != null) {
         for (int i1 = 0; i1 < 61; i1++) {
            if (i1 != 41 && i1 != 0 && i1 != 9 && i1 != 18) {
               unloadSprite(sprites[i1], true);
               sprites[i1] = null;
            }
         }
      }

      if (spriteInstances != null) {
         for (int j1 = 0; j1 < 6; j1++) {
            if (spriteInstances[j1] != null) {
               unloadSprite(spriteInstances[j1].sprite, true);
               spriteInstances[j1].sprite = null;
               spriteInstances[j1] = null;
            }
         }
      }

      objectState = null;
      tileMap = null;
      objectTimers = null;
      objectMap = null;
      tileRedrawTimers = null;
      checkpointObjectState = null;
      checkpointTileMap = null;
      checkpointObjectTimers = null;
      checkpointObjectMap = null;
      checkpointSwitchStates = null;
      this.neededGenSprites = 0L;
      this.rollerVariants = 0;
      this.hintIconSet = -1;
      this.pickupIconSet = -1;
      this.pickupValue = -1;
      this.pickupObject = -1;
      paintSprite = null;
      this.paintSpriteAnim = -1;
      this.enemyPhase = 3;
      tileSlots = null;
      checkpointTileSlots = null;
      actors = null;
      checkpointActors = null;
      enemies = null;
      checkpointEnemies = null;
      this.enemyRemovalPending = false;
      this.enemyChaseTicks = -1;
      this.enemyChaseSteps = 0;
      this.enemyUnusedS = 0;
      this.enemyChaseDirection = 0;
      this.enemyTriggerX = 0;
      this.enemyTriggerY = 0;
      this.enemyTriggerSide = 0;
      this.enemyActorSlot = 0;
      this.enemyTargetRow = 0;
      this.enemyStepTimer = -1;
      this.hasEnemies = false;
      collapsingRows = null;
      splashTop = null;
      splashMiddle = null;
      splashBottom = null;
      mapPreview = null;
      mapPreviewGraphics = null;
      System.gc();
      System.gc();
   }

   private void invalidateHud() {
      this.hudDirty = true;
   }

   private void freeWaterImages() {
      if ((this.neededExtraSprites & 1L) == 0L) {
         for (int i = 0; i < 5; i++) {
            sprites[13].moduleImages[0][i] = null;
         }
      }
   }

   private void loadBackgroundSprite(int var1) {
      try {
         if (var1 == 0) {
            this.stagePackStream = this.getClass().getResourceAsStream("/b0.f");
            this.genPackEntryCount = this.stagePackStream.read();
            genPackDirectory = new byte[this.genPackEntryCount << 3];
            this.stagePackStream.read(genPackDirectory);
         }

         byte[] abyte = new byte[readInt(genPackDirectory, (var1 << 3) + 4)];
         this.stagePackStream.read(abyte);
         Sprite sprite;
         (sprite = new Sprite()).load(abyte, 0);
         sprite.buildModuleImages(0, 0, -1, -1);
         sprite.moduleData = null;
         switch (var1) {
            case 0:
               spriteInstances[5] = new SpriteInstance(sprite, 0, 0, null);
               return;
            case 1:
               sprites[40] = sprite;
         }
      } catch (Exception exception) {
      }
   }

   private void loadMapSprite(int var1) {
      try {
         if (var1 == 0) {
            this.stagePackStream = this.getClass().getResourceAsStream("/mm0.f");
            this.genPackEntryCount = this.stagePackStream.read();
            genPackDirectory = new byte[this.genPackEntryCount << 3];
            this.stagePackStream.read(genPackDirectory);
         }

         byte[] abyte = new byte[readInt(genPackDirectory, (var1 << 3) + 4)];
         this.stagePackStream.read(abyte);
         Sprite sprite;
         (sprite = new Sprite()).load(abyte, 0);
         sprite.buildModuleImages(0, 0, -1, -1);
         sprite.moduleData = null;
         switch (var1) {
            case 0:
               spriteInstances[1] = new SpriteInstance(sprite, 0, 0, null);
               spriteInstances[1].setAnim(2);
            default:
               return;
            case 1:
               spriteInstances[2] = new SpriteInstance(sprite, 0, 0, null);
               spriteInstances[2].setAnim(0);
               return;
            case 2:
               imageSets[27] = sprite.moduleImages[0];
         }
      } catch (Exception exception) {
      }
   }

   private static boolean hasStartedGame() {
      return saveData == null ? false : saveData[13] != 0;
   }

   private void markGameStarted(boolean var1) {
      saveData[13] = 1;
      this.writeSave();
   }

   private void saveStageResult() {
      this.saveProgress(true);
   }

   private void saveProgress(boolean var1) {
      this.saveResultOnLoad = false;
      this.markGameStarted(true);
      this.storeLives();
      this.writePreferences();
      raiseWorldProgress(this.world, unlockedStage);
      if (var1) {
         int l = this.world;
         int i1 = this.stage;
         int k = this.stageRedDiamonds + this.getStageDiamonds(this.world, this.stage);
         int j = i1;
         int i = l;
         saveData[stageRecordOffset(i, j)] = (byte)k;
      }

      this.writeSave();
   }

   private void loadSave() {
      RecordStore recordstore = null;

      try {
         saveData = (recordstore = RecordStore.openRecordStore("DiamondRush", true)).getRecord(1);
         this.saveDataLength = saveData.length;
         recordstore.closeRecordStore();
         return;
      } catch (Exception exception1) {
      } finally {
         try {
            if (recordstore != null) {
               recordstore.closeRecordStore();
            }
         } catch (Exception exception) {
         }
      }
   }

   private void writeSave() {
      this.writeSaveOnLoad = false;
      RecordStore recordstore = null;

      try {
         if ((recordstore = RecordStore.openRecordStore("DiamondRush", true)).getNumRecords() == 0) {
            recordstore.addRecord(saveData, 0, this.saveDataLength);
         } else {
            recordstore.setRecord(1, saveData, 0, this.saveDataLength);
         }

         recordstore.closeRecordStore();
         return;
      } catch (Exception exception1) {
      } finally {
         try {
            if (recordstore != null) {
               recordstore.closeRecordStore();
            }
         } catch (Exception exception) {
         }
      }
   }

   private void writePreferences() {
      RecordStore recordstore = null;

      try {
         if ((recordstore = RecordStore.openRecordStore("Preferences", true)).getNumRecords() == 0) {
            recordstore.addRecord(preferences, 0, this.preferencesLength);
         } else {
            recordstore.setRecord(1, preferences, 0, this.preferencesLength);
         }

         recordstore.closeRecordStore();
         recordstore = RecordStore.openRecordStore("Preferences", true);
         return;
      } catch (Exception exception1) {
      } finally {
         try {
            if (recordstore != null) {
               recordstore.closeRecordStore();
            }
         } catch (Exception exception) {
         }
      }
   }

   private void createSaveStep(int var1) {
      RecordStore recordstore = null;

      try {
         String s = null;
         switch (var1) {
            case 0:
            case 1:
               try {
                  recordstore = RecordStore.openRecordStore("DiamondRush", false);
               } catch (Exception exception1) {
               }

               GameCanvas gamecanvas;
               boolean flag;
               if (recordstore == null) {
                  gamecanvas = this;
                  flag = true;
               } else {
                  gamecanvas = this;
                  flag = false;
               }

               gamecanvas.noSaveFound = flag;
               recordstore.closeRecordStore();
               if (var1 != 0) {
                  (saveData = new byte[1001])[3] = 5;
                  saveData[8] = 4;
                  saveData[9] = 0;
                  saveData[10] = 0;
                  this.saveDataLength = 14;
                  this.saveDataLength += 6;
               }

               return;
            case 2:
               s = "/map_angkor.out";
            case 3:
               if (s == null) {
                  s = "/map_scotland.out";
               }
            case 4:
               if (s == null) {
                  s = "/map_tibet.out";
               }

               this.stagePackStream = this.getClass().getResourceAsStream(stagePackFiles[var1 - 2]);
               this.stagePackStream.read();
               var1 -= 2;
               int i = this.saveDataLength;
               saveData[14 + (var1 << 1)] = (byte)this.saveDataLength;
               saveData[14 + (var1 << 1) + 1] = (byte)(this.saveDataLength >> 8);
               var1 = this.stagePackStream.read();
               saveData[this.saveDataLength++] = (byte)var1;
               saveData[this.saveDataLength++] = 0;
               this.resetMapNodes();
               this.loadMapFile(s);
               saveData[this.saveDataLength++] = (byte)this.firstSecretStage;
               this.unloadMapResources();
               this.saveDataLength += var1 << 1;

               for (int k1 = 0; k1 < var1; k1++) {
                  int j = this.saveDataLength;
                  saveData[i + 3 + (k1 << 1)] = (byte)j;
                  saveData[i + 3 + (k1 << 1) + 1] = (byte)(j >> 8);
                  byte b1 = 0;
                  byte b0 = 0;
                  int k = readShort(this.stagePackStream);
                  int l = readShort(this.stagePackStream);
                  byte[] abyte = new byte[k * l];
                  this.stagePackStream.read(abyte);

                  for (int i1 = 0; i1 < l; i1++) {
                     for (int j1 = 0; j1 < k; j1++) {
                        if (abyte[j1 + i1 * k] == 2) {
                           b0++;
                        }
                     }
                  }

                  saveData[0] = (byte)(saveData[0] + b0);
                  saveData[this.saveDataLength++] = 0;
                  saveData[this.saveDataLength++] = b0;
                  saveData[this.saveDataLength++] = 0;
                  this.stagePackStream.skip(k * l);
                  int i2 = this.saveDataLength;
                  this.saveDataLength += 2;
                  this.stagePackStream.read(abyte);

                  for (int j2 = 0; j2 < l; j2++) {
                     for (int l1 = 0; l1 < k; l1++) {
                        if (abyte[l1 + j2 * k] == 14 || abyte[l1 + j2 * k] == 33) {
                           saveData[this.saveDataLength++] = (byte)l1;
                           saveData[this.saveDataLength++] = (byte)j2;
                           b1++;
                        }
                     }
                  }

                  saveData[i2] = 0;
                  saveData[i2 + 1] = b1;
               }

               this.stagePackStream.close();
               this.stagePackStream = null;
               System.gc();
               return;
            case 5:
            default:
               return;
         }
      } catch (Exception exception2) {
      } finally {
         try {
            if (recordstore != null) {
               recordstore.closeRecordStore();
            }
         } catch (Exception exception) {
         }
      }
   }

   private void storeLives() {
      saveData[3] = (byte)this.lives;
      saveData[11] = this.checkpointBannerCount;
   }

   private void loadLives() {
      this.reloadLivesOnLoad = false;
      this.lives = saveData[3];
      this.checkpointBannerCount = saveData[11];
      this.stageDiamonds = readShort(saveData, 4);
   }

   private byte getStageFlags(int var1, int var2) {
      int i = stageRecordOffset(var1, var2);
      return saveData[i + 2];
   }

   private void addStageFlags(int var1, int var2, byte var3) {
      int i = stageRecordOffset(var1, var2);
      saveData[i + 2] = (byte)(saveData[i + 2] | var3);
   }

   private static void raiseWorldProgress(int var0, int var1) {
      var0 = worldRecordOffset(var0) + 1;
      if (saveData[var0] < var1) {
         saveData[var0] = (byte)var1;
      }
   }

   private static int getWorldProgress(int var0) {
      int i;
      return (i = worldRecordOffset(var0) + 1) >= 0 && i <= saveData.length ? saveData[worldRecordOffset(var0) + 1] : 0;
   }

   private static int worldRecordOffset(int var0) {
      byte b1;
      if (var0 < 0) {
         b1 = 0;
      } else {
         if (var0 < 3) {
            return readShort(saveData, 14 + (var0 << 1));
         }

         b1 = 2;
      }

      byte b0 = b1;
      return readShort(saveData, 14 + (b0 << 1));
   }

   private static int getStageCount(int var0) {
      try {
         return saveData[worldRecordOffset(var0)];
      } catch (Exception exception) {
         return 0;
      }
   }

   private static int getMainStageCount(int var0) {
      return saveData[worldRecordOffset(var0) + 2];
   }

   private static int stageRecordOffset(int var0, int var1) {
      return readShort(saveData, worldRecordOffset(var0) + 3 + (var1 << 1));
   }

   private int getStageDiamonds(int var1, int var2) {
      return saveData[stageRecordOffset(var1, var2)];
   }

   private int getStageDiamondTotal(int var1, int var2) {
      return saveData[stageRecordOffset(var1, var2) + 1];
   }

   private void markTreasureTaken(int var1, int var2, int var3, int var4) {
      int i = stageRecordOffset(var1, var2);
      byte b0 = saveData[i + 4];

      for (int j = 0; j < b0; j++) {
         if (saveData[i + 5 + j * 2] == var3 && saveData[i + 5 + j * 2 + 1] == var4) {
            saveData[i + 5 + j * 2] = 0;
            saveData[i + 5 + j * 2 + 1] = 0;
            saveData[i + 3]++;
            return;
         }
      }
   }

   private boolean isTreasureAvailable(int var1, int var2, int var3, int var4) {
      int i = stageRecordOffset(var1, var2);
      byte b0 = saveData[i + 4];

      for (int j = 0; j < b0; j++) {
         if (saveData[i + 5 + j * 2] == var3 && saveData[i + 5 + j * 2 + 1] == var4) {
            return false;
         }
      }

      return true;
   }

   private void initBossFight() {
      this.bossAnim = 13;
      this.bossHealth = 4;
      this.bossX = 408;
      this.stageKind = 5;
      this.neededGenSprites |= 128L;
      this.neededGenSprites |= 8L;
      this.knightTurning = false;
      this.arenaWallRow = 16;
      this.arenaWallX1 = 16;
      this.arenaWallX2 = 19;
      this.arenaWallRow2 = 18;
   }

   private void unlockNextStages() {
      int i = this.mapCursorX;
      int j = this.mapCursorY;
      long k;
      int l = longBits(k = mapNodes[i][j], (byte)6, (byte)5);
      int i1 = longBits(k, (byte)11, (byte)3);
      int j1 = 14;
      if (this.stage >= this.firstSecretStage) {
         this.addStageFlags(this.world, this.stage, (byte)2);
      }

      if (i1 > 1) {
         int k1 = 0;

         while (k1 < i1) {
            int l1 = longBits(k, (byte)j1, (byte)4);
            j1 += 4;
            int i2 = longBits(k, (byte)j1, (byte)4);
            long j2;
            if (longBits(j2 = mapNodes[l1][i2], (byte)3, (byte)3) == 1 && (l1 = longBits(j2, (byte)6, (byte)5)) > l) {
               unlockedStage = l1;
               this.unlockedNodeX = i;
               this.unlockedNodeY = j;
               this.addStageFlags(this.world, unlockedStage, (byte)64);
               this.stageJustUnlocked = true;
            }

            k1++;
            j1 += 4;
         }
      } else {
         unlockedStage = this.stage;
      }

      this.saveProgress(false);
   }

   public final void paint(Graphics var1) {
      try {
         if (this.getWidth() < this.getHeight()) {
            try {
               Graphics graphics = var1;
               GameCanvas gamecanvas9 = this;

               try {
                  graphics.setColor(0);
                  graphics.setClip(0, 0, gamecanvas9.getWidth(), gamecanvas9.getHeight());
                  graphics.fillRect(0, 0, gamecanvas9.getWidth(), gamecanvas9.getHeight());
                  graphics.setColor(16777215);
                  int l1 = gamecanvas9.getWidth() >> 1;
                  int j3 = (gamecanvas9.getHeight() >> 1) - 40;
                  Cutscene.drawWrappedCentered(graphics, Text.get(34), l1, j3, gamecanvas9.getWidth() - 100);
               } catch (Exception exception) {
               }

               this.hideNotify();
               this.showNotify();
            } catch (Exception exception1) {
               exception1.printStackTrace();
            }
         } else {
            try {
               this.screen = var1;
               this.screen.setClip(0, 0, 320, 240);
               switch (gameState) {
                  case 0:
                  case 3:
                  case 10:
                  case 11:
                  case 13:
                  case 14:
                  case 19:
                  case 23:
                  case 26:
                  case 32:
                  default:
                     break;
                  case 1:
                     this.paintPlaying();
                     break;
                  case 2:
                     this.paintMenu();
                     break;
                  case 4:
                     this.paintMenu();
                     break;
                  case 5:
                     if (!this.tipVisible) {
                        this.screen.setClip(0, Constants.loadingTextY, 320, 240 - Constants.loadingTextY);
                        this.paintLoading();
                        this.screen.setClip(0, 0, 320, 240);
                     } else {
                        this.paintLoading();
                     }

                     if (tipText != null && this.tipVisible) {
                        this.paintTip();
                     }
                     break;
                  case 6:
                     GameCanvas gamecanvas7 = this;
                     SpriteInstance spriteinstance = spriteInstances[0];
                     gamecanvas7.screen.setColor(0);
                     gamecanvas7.screen.fillRect(0, 0, 320, 240);
                     spriteinstance.y = 96;
                     int l2 = 0;
                     if (stateTicks > 40) {
                        spriteinstance.x = 179;
                        spriteinstance.setAnim(1);
                        int i6;
                        if ((i6 = stateTicks - 40 << 2) > 29) {
                           i6 = 29;
                           Sprite sprite4;
                           Graphics graphics4;
                           byte b5;
                           short short2;
                           int j9;
                           if (stateTicks < 42) {
                              int l7 = 42 - stateTicks;
                              l2 = 0 + l7 * l7 % ((l7 >> 1) + 1);
                              sprite4 = spriteinstance.sprite;
                              graphics4 = gamecanvas7.screen;
                              b5 = 10;
                              short2 = 178;
                              j9 = l2 + 96;
                           } else {
                              sprite4 = spriteinstance.sprite;
                              graphics4 = gamecanvas7.screen;
                              b5 = 4;
                              short2 = 178;
                              j9 = 96;
                           }

                           sprite4.paintFrame(graphics4, b5, short2, j9, 0, 0, 0);
                           spriteinstance.animFrame = 0;
                        } else {
                           spriteinstance.sprite.paintFrame(gamecanvas7.screen, 10, 178, 96, 0, 0, 0);
                        }

                        spriteinstance.y = i6 + 96;
                     } else {
                        spriteinstance.x = (stateTicks << 2) - 1 + 18;
                     }

                     spriteinstance.y += l2;
                     spriteinstance.paint(gamecanvas7.screen);
                     spriteinstance.sprite.paintFrame(gamecanvas7.screen, 5, 178, l2 + 120, 0, 0, 0);
                     break;
                  case 7:
                     this.paintMenu();
                     break;
                  case 8:
                  case 9:
                  case 21:
                  case 35:
                     this.paintLoading();
                     break;
                  case 12:
                     GameCanvas gamecanvas6 = this;
                     this.screen.setColor(0);
                     gamecanvas6.screen.fillRect(0, 0, 320, 240);
                     FontRenderer fontrenderer1 = fontSmall;
                     fontSmall.drawString(gamecanvas6.screen, strings[26], 160, 50, 17);
                     FontRenderer fontrenderer7;
                     Graphics graphics3;
                     String s11;
                     short short1;
                     int i9;
                     byte b7;
                     if (gamecanvas6.stageKind == 2) {
                        fontrenderer7 = fontrenderer1;
                        graphics3 = gamecanvas6.screen;
                        s11 = strings[49];
                        short1 = 0;
                        i9 = 240 - (fontrenderer1.font.getHeight() - 10);
                        b7 = 36;
                     } else {
                        fontrenderer1.drawString(gamecanvas6.screen, strings[16], 0, 240 - (fontrenderer1.font.getHeight() - 10), 36);
                        String s4 = strings[126] + " " + (readShort(saveData, 4) < 500 ? readShort(saveData, 4) : 500) + " " + strings[19];
                        fontrenderer7 = fontrenderer1;
                        graphics3 = gamecanvas6.screen;
                        s11 = s4;
                        short1 = 160;
                        i9 = 160;
                        b7 = 17;
                     }

                     fontrenderer7.drawString(graphics3, s11, short1, i9, b7);
                     break;
                  case 15:
                     GameCanvas gamecanvas5 = this;
                     if (this.mapPreviewStale) {
                        mapPreview = Image.createImage(200, 146);
                     }

                     if (gamecanvas5.fullRedraw) {
                        GameCanvas gamecanvas10;
                        int i1;
                        int i2;
                        int l4;
                        byte b3;
                        label477: {
                           gamecanvas5.fullRedraw = false;
                           gamecanvas5.mapCursorMoving = true;
                           gamecanvas10 = gamecanvas5;
                           gamecanvas5.screen.setClip(0, 0, 320, 240);
                           i1 = 0;
                           i2 = 0;
                           l4 = 0;
                           b3 = -1;
                           byte b4;
                           switch (gamecanvas10.world) {
                              case 0:
                                 i1 = 939282;
                                 i2 = 3111750;
                                 l4 = 8635434;
                                 b4 = 1;
                                 break;
                              case 1:
                                 i1 = 869201;
                                 i2 = 4022666;
                                 l4 = 5873874;
                                 b4 = 4;
                                 break;
                              case 2:
                                 i1 = 5210510;
                                 i2 = 3711421;
                                 l4 = 7469567;
                                 b4 = 64;
                                 break;
                              default:
                                 break label477;
                           }

                           b3 = b4;
                        }

                        gamecanvas10.screen.setColor(i1);
                        gamecanvas10.screen.fillRect(0, 0, 320, 240);
                        fontMedium.drawString(gamecanvas10.screen, strings[b3], 160, 12, 17);
                        (mapPreviewGraphics = mapPreview.getGraphics()).setColor(i1);
                        mapPreviewGraphics.fillRect(0, 0, 200, 146);
                        sprites[23].paintFrame(mapPreviewGraphics, 0, 100, 66, 0, 0, 0);
                        gamecanvas10.screen.setColor(i2);
                        gamecanvas10.screen.fillRoundRect(2, 198, 316, Constants.mapStatsBoxHeight, 8, 8);
                        gamecanvas10.screen.setColor(l4);
                        gamecanvas10.screen.drawRoundRect(2, 198, 316, Constants.mapStatsBoxHeight, 8, 8);
                        gamecanvas10.drawSelectSoftKey();
                        gamecanvas10.drawBackSoftKey();
                        fontSmall.drawString(gamecanvas10.screen, strings[60], 298, 234, 40);
                        if (sprites[17] != null) {
                           sprites[17].paintFrame(gamecanvas10.screen, 12, 10, 201, 0, 0, 0);
                           sprites[17].paintFrame(gamecanvas10.screen, 10, 230, 203, 0, 0, 0);
                           sprites[17].paintFrame(gamecanvas10.screen, 11, 130, 203, 0, 0, 0);
                        }

                        textBuffer.delete(0, textBuffer.length());
                        textBuffer.append(gamecanvas10.lives);
                        fontSmall.drawString(gamecanvas10.screen, textBuffer.toString(), 42, 213, 20);
                        textBuffer.delete(0, textBuffer.length());
                        textBuffer.append(readShort(saveData, 4));
                        fontSmall.drawString(gamecanvas10.screen, textBuffer.toString(), 150, 213, 20);
                        textBuffer.delete(0, textBuffer.length());
                        int redTotal = readShort(saveData, 6);
                        byte b2 = saveData[0];
                        if (redTotal >= b2) {
                           textBuffer.append(redTotal).append("/").append(redTotal);
                        } else {
                           textBuffer.append(redTotal).append("/").append(b2);
                        }

                        fontSmall.drawString(gamecanvas10.screen, textBuffer.toString(), 250, 213, 20);
                        gamecanvas10.mapPreviewStale = false;
                        gamecanvas5.paintMapNodes();
                     }

                     if (gamecanvas5.mapCursorMoving) {
                        GameCanvas gamecanvas11;
                        int j1;
                        int j2;
                        label468: {
                           gamecanvas5.screen.drawImage(mapPreview, 60, 51, 0);
                           gamecanvas11 = gamecanvas5;
                           j1 = 0;
                           j2 = 0;
                           int j8;
                           switch (gamecanvas11.world) {
                              case 0:
                                 j1 = 3111750;
                                 j8 = 8635434;
                                 break;
                              case 1:
                                 j1 = 4022666;
                                 j8 = 5873874;
                                 break;
                              case 2:
                                 j1 = 3711421;
                                 j8 = 7469567;
                                 break;
                              default:
                                 break label468;
                           }

                           j2 = j8;
                        }

                        gamecanvas11.screen.setColor(j1);
                        gamecanvas11.screen.fillRoundRect(2, Constants.mapTitleBoxY, 316, Constants.mapTitleBoxHeight, 8, 8);
                        gamecanvas11.screen.setColor(j2);
                        gamecanvas11.screen.drawRoundRect(2, Constants.mapTitleBoxY, 316, Constants.mapTitleBoxHeight, 8, 8);
                        gamecanvas11 = gamecanvas5;
                        j1 = 77 + gamecanvas11.mapCursorX * 13 + 6;
                        j2 = 33 + gamecanvas11.mapCursorY * 13 + 6;
                        if (gamecanvas11.mapCursorX != gamecanvas11.mapTargetX || gamecanvas11.mapCursorY != gamecanvas11.mapTargetY) {
                           int j5 = 77 + gamecanvas11.mapTargetX * 13 + 6;
                           int j7 = 33 + gamecanvas11.mapTargetY * 13 + 6;
                           if ((gamecanvas11.getStageFlags(gamecanvas11.world, gamecanvas11.stage + 1) & 2) != 0 && gamecanvas11.stage + 1 == unlockedStage
                              || gamecanvas11.stage == unlockedStage) {
                              gamecanvas11.mapUnlockDone = true;
                           }

                           if (!gamecanvas11.mapUnlockDone) {
                              gamecanvas11.mapUnlockDone = gamecanvas11.animateMapUnlock();
                              if (gamecanvas11.mapUnlockDone) {
                                 gamecanvas11.fullRedraw = true;
                              }
                           } else {
                              int k = j7;
                              int k2 = j5;
                              int k1 = j2;
                              j5 = j1;
                              GameCanvas gamecanvas12 = gamecanvas11;
                              if (gamecanvas11.mapWalkStarting) {
                                 gamecanvas12.mapWalkStarting = false;
                                 gamecanvas12.mapWalkX = j5;
                                 gamecanvas12.mapWalkY = k1;
                                 gamecanvas12.mapWalkError = 0;
                                 gamecanvas12.mapWalkDx = k2 - j5;
                                 gamecanvas12.mapWalkDy = k - k1;
                                 gamecanvas12.mapWalkErrorStep = 0;
                                 gamecanvas12.mapWalkErrorInc = 0;
                                 gamecanvas12.mapWalkStepX = 10;
                                 gamecanvas12.mapWalkStepY = 10;
                              }

                              if (gamecanvas12.mapWalkDx < 0) {
                                 gamecanvas12.mapWalkStepX = -10;
                                 gamecanvas12.mapWalkDx = -gamecanvas12.mapWalkDx;
                              }

                              if (gamecanvas12.mapWalkDy < 0) {
                                 gamecanvas12.mapWalkStepY = -10;
                                 gamecanvas12.mapWalkDy = -gamecanvas12.mapWalkDy;
                              }

                              boolean flag1;
                              label441: {
                                 label440: {
                                    gamecanvas12.mapWalkFacingLeft = gamecanvas12.mapWalkStepX <= 0;
                                    j1 = gamecanvas12.mapWalkFacingLeft ? 7 : 6;
                                    if (gamecanvas12.mapWalkDy <= gamecanvas12.mapWalkDx) {
                                       gamecanvas12.mapWalkErrorStep = 2 * gamecanvas12.mapWalkDx;
                                       gamecanvas12.mapWalkErrorInc = 2 * gamecanvas12.mapWalkDy;
                                       if (gamecanvas12.mapWalkStepX < 0 && gamecanvas12.mapWalkX <= k2
                                          || gamecanvas12.mapWalkStepX > 0 && gamecanvas12.mapWalkX >= k2) {
                                          j2 = 77 + gamecanvas12.mapTargetX * 13 + 6;
                                          k = 33 + gamecanvas12.mapTargetY * 13 + 6;
                                          sprites[17].paintFrame(gamecanvas12.screen, j1, j2, k, 0, 0, 0);
                                          gamecanvas12.mapWalkStarting = true;
                                          flag1 = true;
                                          break label441;
                                       }

                                       sprites[17].paintFrame(gamecanvas12.screen, j1, gamecanvas12.mapWalkX, gamecanvas12.mapWalkY, 0, 0, 0);
                                       gamecanvas12.mapWalkX = gamecanvas12.mapWalkX + gamecanvas12.mapWalkStepX;
                                       gamecanvas12.mapWalkError = gamecanvas12.mapWalkError + gamecanvas12.mapWalkErrorInc;
                                       if (gamecanvas12.mapWalkError <= gamecanvas12.mapWalkDx) {
                                          break label440;
                                       }

                                       gamecanvas12.mapWalkY = gamecanvas12.mapWalkY + gamecanvas12.mapWalkStepY;
                                    } else {
                                       gamecanvas12.mapWalkErrorStep = 2 * gamecanvas12.mapWalkDy;
                                       gamecanvas12.mapWalkErrorInc = 2 * gamecanvas12.mapWalkDx;
                                       if (gamecanvas12.mapWalkStepY < 0 && gamecanvas12.mapWalkY <= k
                                          || gamecanvas12.mapWalkStepY > 0 && gamecanvas12.mapWalkY >= k) {
                                          j2 = 77 + gamecanvas12.mapTargetX * 13 + 6;
                                          k = 33 + gamecanvas12.mapTargetY * 13 + 6;
                                          sprites[17].paintFrame(gamecanvas12.screen, j1, j2, k, 0, 0, 0);
                                          gamecanvas12.mapWalkStarting = true;
                                          flag1 = true;
                                          break label441;
                                       }

                                       sprites[17].paintFrame(gamecanvas12.screen, j1, gamecanvas12.mapWalkX, gamecanvas12.mapWalkY, 0, 0, 0);
                                       gamecanvas12.mapWalkY = gamecanvas12.mapWalkY + gamecanvas12.mapWalkStepY;
                                       gamecanvas12.mapWalkError = gamecanvas12.mapWalkError + gamecanvas12.mapWalkErrorInc;
                                       if (gamecanvas12.mapWalkError <= gamecanvas12.mapWalkDy) {
                                          break label440;
                                       }

                                       gamecanvas12.mapWalkX = gamecanvas12.mapWalkX + gamecanvas12.mapWalkStepX;
                                    }

                                    gamecanvas12.mapWalkError = gamecanvas12.mapWalkError - gamecanvas12.mapWalkErrorStep;
                                 }

                                 flag1 = false;
                              }

                              if (flag1) {
                                 gamecanvas11.mapCursorX = gamecanvas11.mapTargetX;
                                 gamecanvas11.mapCursorY = gamecanvas11.mapTargetY;
                              }
                           }
                        } else if (gamecanvas11.mapCursorMoving) {
                           int i5 = gamecanvas11.mapWalkFacingLeft ? 7 : 6;
                           sprites[17].paintFrame(gamecanvas11.screen, i5, j1, j2, 0, 0, 0);
                        }

                        FontRenderer fontrenderer6;
                        Graphics graphics2;
                        String s10;
                        int k8;
                        int l8;
                        byte b6;
                        label417: {
                           gamecanvas11 = gamecanvas5;
                           int k5;
                           String[] astring;
                           if ((k5 = longBits(mapNodes[gamecanvas11.mapCursorX][gamecanvas11.mapCursorY], (byte)6, (byte)5)) < gamecanvas11.firstSecretStage) {
                              if (rightToLeft) {
                                 fontrenderer6 = fontMedium;
                                 graphics2 = gamecanvas11.screen;
                                 s10 = stageNames[k5];
                                 k8 = 234;
                                 l8 = Constants.mapTitleTextY;
                                 b6 = 40;
                                 break label417;
                              }

                              fontrenderer6 = fontMedium;
                              graphics2 = gamecanvas11.screen;
                              astring = stageNames;
                              k8 = k5;
                           } else {
                              if (rightToLeft) {
                                 fontrenderer6 = fontMedium;
                                 graphics2 = gamecanvas11.screen;
                                 s10 = stageNames[k5 + 10 - gamecanvas11.firstSecretStage + 1];
                                 k8 = 234;
                                 l8 = Constants.mapTitleTextY;
                                 b6 = 40;
                                 break label417;
                              }

                              fontrenderer6 = fontMedium;
                              graphics2 = gamecanvas11.screen;
                              astring = stageNames;
                              k8 = k5 + 10 - gamecanvas11.firstSecretStage + 1;
                           }

                           s10 = astring[k8];
                           k8 = 8;
                           l8 = Constants.mapTitleTextY;
                           b6 = 36;
                        }

                        fontrenderer6.drawString(graphics2, s10, k8, l8, b6);
                        gamecanvas11 = gamecanvas5;
                        if (gamecanvas5.mapCursorX == gamecanvas11.mapTargetX && gamecanvas11.mapCursorY == gamecanvas11.mapTargetY) {
                           int l5 = longBits(mapNodes[gamecanvas11.mapCursorX][gamecanvas11.mapCursorY], (byte)6, (byte)5);
                           int k7 = gamecanvas11.getStageDiamonds(gamecanvas11.world, l5);
                           l5 = gamecanvas11.getStageDiamondTotal(gamecanvas11.world, l5);
                           if (k7 > l5) {
                              k7 = l5;
                           }

                           textBuffer.delete(0, textBuffer.length());
                           textBuffer.append(k7);
                           textBuffer.append('/');
                           textBuffer.append(l5);
                           fontSmall.measure(textBuffer.toString());
                           l5 = FontRenderer.measuredWidth + 6 + 14;
                           j2 = 77 + gamecanvas11.mapCursorX * 13 + 6;
                           int i8 = 33 + gamecanvas11.mapCursorY * 13 + 6;
                           j1 = j2 - (l5 >> 1);
                           if ((k7 = i8 - 17 + 21) <= 50) {
                              k7 = 50;
                              if ((j1 = j2 + 20) + l5 >= 280) {
                                 j1 = j2 - l5 + -20;
                              }
                           }

                           if (j1 <= 25) {
                              j1 = 25;
                           }

                           if (j1 + l5 >= 280) {
                              j1 = 230;
                           }

                           drawFramedBox(gamecanvas11.screen, j1, k7 - 15, l5, 17, 37042, 0);
                           fontSmall.drawString(gamecanvas11.screen, textBuffer.toString(), j1 + 2, k7 + 2 + 8, 20);
                           if (sprites[17] != null) {
                              sprites[17].paintFrame(gamecanvas11.screen, 10, j1 + l5 - 2 - 14, k7 + 2 - 1, 0, 0, 0);
                           }
                        }
                     }

                     if (gamecanvas5.mapCursorMoving && gamecanvas5.mapCursorX == gamecanvas5.mapTargetX && gamecanvas5.mapCursorY == gamecanvas5.mapTargetY) {
                        gamecanvas5.mapCursorMoving = false;
                     }
                     break;
                  case 16:
                     this.paintLoading();
                     break;
                  case 17:
                     FontRenderer fontrenderer4;
                     GameCanvas gamecanvas4 = this;
                     label493: {
                        int l;
                        Graphics graphics1;
                        label519: {
                           label520: {
                              l = this.subState;
                              fontrenderer4 = fontSmall;
                              graphics1 = gamecanvas4.screen;
                              gamecanvas4.screen.setColor(2496263);
                              graphics1.fillRect(0, 0, 320, 240);
                              switch (l) {
                                 case 0:
                                    break label519;
                                 case 1:
                                    break label520;
                                 case 2:
                                    break;
                                 case 5:
                                    if (gamecanvas4.stageRetries == 0) {
                                       if ((gamecanvas4.stageAwards & 32) != 0) {
                                          graphics1.drawImage(imageSets[5][0], 288, 176, 0);
                                          if (l == 5) {
                                             Sprite sprite;
                                             int k6 = (sprite = sprites[9]).getAnimFrameCount(0);
                                             if (stateTicks < k6 << 1) {
                                                sprite.paintAnimFrame(graphics1, 0, stateTicks << 1, 288, 176, 0, 0, 0);
                                             }
                                          }
                                       }

                                       graphics1.drawImage(imageSets[28][0], 268, 178, 0);
                                    }
                                 case 4:
                                    int k3;
                                    if (l != 4 || (k3 = -100 + stateTicks * 10) > 0) {
                                       k3 = 0;
                                    }

                                    spriteInstances[0].sprite.paintAnimFrame(graphics1, 12, 0, k3 + 7, 175, 0, 0, 0);
                                    fontrenderer4.drawLines(graphics1, strings[78], 160, 182, 17);
                                    String s5 = String.valueOf(gamecanvas4.stageRetries);
                                    fontrenderer4.drawLines(graphics1, s5, 160, 200, 17);
                                    if (gamecanvas4.stageHits == 0) {
                                       if ((gamecanvas4.stageAwards & 16) != 0) {
                                          graphics1.drawImage(imageSets[5][0], 288, 136, 0);
                                          if (l == 4) {
                                             Sprite sprite1;
                                             k3 = (sprite1 = sprites[9]).getAnimFrameCount(0);
                                             if (stateTicks < k3 << 1) {
                                                sprite1.paintAnimFrame(graphics1, 0, stateTicks >> 1, 288, 136, 0, 0, 0);
                                             }
                                          }
                                       }

                                       graphics1.drawImage(imageSets[28][0], 268, 138, 0);
                                    }
                                 case 3:
                                    int l3;
                                    if (l != 3 || (l3 = -100 + stateTicks * 10) > 0) {
                                       l3 = 0;
                                    }

                                    spriteInstances[0].sprite.paintAnimFrame(graphics1, 10, 0, l3 + 7, 139, 0, 0, 0);
                                    fontrenderer4.drawLines(graphics1, strings[68], 160, 142, 17);
                                    String s6 = String.valueOf(gamecanvas4.stageHits);
                                    fontrenderer4.drawLines(graphics1, s6, 160, 160, 17);
                                    if (gamecanvas4.stageRedDiamonds == gamecanvas4.stageRedDiamondTotal) {
                                       if ((gamecanvas4.stageAwards & 8) != 0) {
                                          graphics1.drawImage(imageSets[5][0], 288, 96, 0);
                                          if (l == 3) {
                                             Sprite sprite2;
                                             l3 = (sprite2 = sprites[9]).getAnimFrameCount(0);
                                             if (stateTicks < l3 << 1) {
                                                sprite2.paintAnimFrame(graphics1, 0, stateTicks >> 1, 288, 96, 0, 0, 0);
                                             }
                                          }
                                       }

                                       graphics1.drawImage(imageSets[28][0], 268, 98, 0);
                                    }
                                    break;
                                 default:
                                    break label493;
                              }

                              int i4;
                              if (l != 2 || (i4 = -100 + stateTicks * 10) > 0) {
                                 i4 = 0;
                              }

                              sprites[themedSpriteIndex(3)].paintFrame(graphics1, 0, i4 + 7, 95, 0, 0, 0);
                              fontrenderer4.drawLines(graphics1, strings[48], 160, 102, 17);
                              String s7 = gamecanvas4.stageRedDiamonds + "/" + gamecanvas4.stageRedDiamondTotal;
                              fontrenderer4.drawLines(graphics1, s7, 160, 120, 17);
                              if (gamecanvas4.stageDiamonds == gamecanvas4.stageDiamondTotal) {
                                 if ((gamecanvas4.stageAwards & 4) != 0) {
                                    graphics1.drawImage(imageSets[5][0], 288, 56, 0);
                                    if (l == 2) {
                                       Sprite sprite3;
                                       i4 = (sprite3 = sprites[9]).getAnimFrameCount(0);
                                       if (stateTicks < i4 >> 1) {
                                          sprite3.paintAnimFrame(graphics1, 0, stateTicks << 1, 288, 56, 0, 0, 0);
                                       }
                                    }
                                 }

                                 graphics1.drawImage(imageSets[28][0], 268, 58, 0);
                              }
                           }

                           int j4;
                           if (l != 1 || (j4 = -100 + stateTicks * 10) > 0) {
                              j4 = 0;
                           }

                           sprites[themedSpriteIndex(2)].paintFrame(graphics1, 0, j4 + 7, 55, 0, 0, 0);
                           fontrenderer4.drawLines(graphics1, strings[19], 160, 62, 17);
                           int l6;
                           if (l != 1 || (l6 = stateTicks >> 1) > gamecanvas4.stageDiamonds) {
                              l6 = gamecanvas4.stageDiamonds;
                           }

                           String s8 = l6 + "/" + gamecanvas4.stageDiamondTotal;
                           fontrenderer4.drawLines(graphics1, s8, 160, 80, 17);
                        }

                        int k4;
                        int i7;
                        label370: {
                           if (l == 0) {
                              i7 = (k4 = -100 + stateTicks * 10) - 320;
                              if (k4 > 0) {
                                 k4 = 0;
                              }

                              if (i7 <= 0) {
                                 break label370;
                              }
                           } else {
                              k4 = 0;
                           }

                           i7 = 0;
                        }

                        fontrenderer4.drawString(graphics1, stageNames[stageNumbers[gamecanvas4.world][gamecanvas4.stage] - 1], k4 + 160, 15, 17);
                        fontrenderer4.drawString(graphics1, strings[14], i7 + 160, 32, 17);
                     }

                     fontSmall.drawString(gamecanvas4.screen, strings[gamecanvas4.subState == 5 ? 17 : 65], 5, 240 - (fontrenderer4.font.getHeight() - 10), 36);
                     break;
                  case 18:
                     GameCanvas gamecanvas3 = this;
                     byte b1 = 0;
                     String s3 = "/ms.f";
                     boolean flag = false;
                     b1 = b1;
                     s3 = "/ms.f";
                     sprites[17] = loadSprite("/ms.f", b1, 0, 0);
                     gamecanvas3.updateShop();
                     this.fullRedraw = true;
                     gameState = 25;
                     this.shownArmorLevel = this.armorLevel;
                     this.shopDiamondsText = strings[125] + " " + readShort(saveData, 4) + " " + strings[19];
                  case 25:
                     this.paintShop();
                     break;
                  case 20:
                     drawMessageBox(this.screen, fontSmall, wrapForScreen(strings[61]), 160, 90, 17, 20, true);
                     break;
                  case 22:
                     this.paintCredits();
                     break;
                  case 24:
                     GameCanvas gamecanvas2 = this;
                     String s2 = null;
                     FontRenderer fontrenderer3 = fontSmall;
                     fontSmall.unusedSpacing = 15;
                     gamecanvas2.screen.setColor(0);
                     gamecanvas2.screen.fillRect(0, 0, 320, 240);

                     for (int i = 0; i < 6; i++) {
                        byte b0;
                        label357: {
                           b0 = -1;
                           String s12;
                           switch (i) {
                              case 0:
                                 b0 = 7;
                                 s12 = " 6 - Add " + String.valueOf(gamecanvas2.stageDiamonds);
                                 break;
                              case 1:
                                 b0 = 8;
                                 s12 = " 7 - Add " + String.valueOf(gamecanvas2.stageRedDiamonds);
                                 break;
                              case 2:
                                 b0 = 12;
                                 s12 = " 8 - x " + String.valueOf(gamecanvas2.cheatWeapons) + (gamecanvas2.cheatWeapons >= 4 ? " blue potion" : "");
                                 break;
                              case 3:
                                 b0 = 11;
                                 s12 = " 9 - " + strings[gamecanvas2.cheatUnlockAllStages ? 43 : 42];
                                 break;
                              case 4:
                                 b0 = 10;
                                 s12 = " 0 - " + strings[gamecanvas2.cheatUnlockSecretStages ? 43 : 42];
                                 break;
                              case 5:
                                 b0 = -1;
                                 s12 = "";
                                 break;
                              default:
                                 break label357;
                           }

                           s2 = s12;
                        }

                        int j = (i + 1) * 30;
                        if (b0 >= 0) {
                           fontrenderer3.drawString(gamecanvas2.screen, strings[b0], 150, j, 17);
                        }

                        j += fontrenderer3.unusedSpacing;
                        FontRenderer fontrenderer5;
                        Graphics graphics5;
                        String s13;
                        short short3;
                        int k9;
                        byte b8;
                        if (b0 == -1) {
                           fontrenderer5 = fontrenderer3;
                           graphics5 = gamecanvas2.screen;
                           s13 = s2;
                           short3 = 150;
                           k9 = j;
                           b8 = 17;
                        } else {
                           fontrenderer3.drawString(gamecanvas2.screen, strings[9], 150, j, 24);
                           fontrenderer5 = fontrenderer3;
                           graphics5 = gamecanvas2.screen;
                           s13 = s2;
                           short3 = 150;
                           k9 = j;
                           b8 = 20;
                        }

                        fontrenderer5.drawString(graphics5, s13, short3, k9, b8);
                     }

                     s2 = "Pound (#) - Pass levels ";
                     StringBuffer stringbuffer;
                     String s9;
                     if (gamecanvas2.cheatSkipStages) {
                        stringbuffer = new StringBuffer().append(s2);
                        s9 = "on";
                     } else {
                        stringbuffer = new StringBuffer().append(s2);
                        s9 = "off";
                     }

                     s2 = stringbuffer.append(s9).toString();
                     fontrenderer3.drawString(gamecanvas2.screen, s2, 150, 240 - 3 * fontrenderer3.getHeight() - 5, 17);
                     fontrenderer3.drawString(gamecanvas2.screen, "(Press 9 while gameplay to", 150, 240 - 2 * fontrenderer3.getHeight() - 5, 17);
                     fontrenderer3.drawString(gamecanvas2.screen, "skip the level)", 150, 240 - fontrenderer3.getHeight() - 5, 17);
                     gamecanvas2.drawSelectSoftKey();
                     gamecanvas2.drawBackSoftKey();
                     break;
                  case 27:
                     this.paintSeal();
                     break;
                  case 28:
                     this.loadingStepCount = 11;
                     this.paintLoading();
                     break;
                  case 29:
                     this.paintEnding();
                     break;
                  case 30:
                     drawSplash(this.screen, true);
                     this.drawSeparator(200);
                     if (stateTicks % 15 >= 7) {
                        fontMedium.drawString(this.screen, strings[46], 160, 200, 17);
                     }
                     break;
                  case 31:
                     if (this.blackScreenPending) {
                        this.blackScreenPending = false;
                        this.screen.setColor(0);
                        this.screen.fillRect(0, 0, 320, 240);
                        fontSmall.drawLines(this.screen, wrapForScreen(strings[this.noSaveFound ? 39 : 21]), 160, 120, 3);
                        this.drawBackSoftKey();
                        this.drawSelectSoftKey();
                     }
                     break;
                  case 33:
                     GameCanvas gamecanvas1 = this;
                     if (this.fullRedraw) {
                        gamecanvas1.screen.setColor(0);
                        gamecanvas1.screen.setClip(0, 0, 320, 240);
                        gamecanvas1.screen.fillRect(0, 0, 320, 240);
                        String s1 = strings[18]
                           + "\n\n"
                           + strings[29]
                           + "\n"
                           + strings[37]
                           + "\n"
                           + strings[38]
                           + "\n\n"
                           + strings[119]
                           + "\n\n"
                           + wrapForScreen(strings[118])
                           + "\n"
                           + wrapForScreen(strings[67]);
                        fontSmall.drawLines(gamecanvas1.screen, s1, 160, 10, 17);
                        gamecanvas1.drawSelectSoftKey();
                        gamecanvas1.fullRedraw = false;
                     }
                     break;
                  case 34:
                     GameCanvas gamecanvas = this;
                     FontRenderer fontrenderer = fontSmall;
                     fontSmall.unusedSpacing = 2;
                     gamecanvas.screen.setColor(0);
                     gamecanvas.screen.fillRect(0, 0, 320, 240);
                     String s;
                     switch (gamecanvas.debugEditorRow) {
                        case 0:
                           s = "Mix";
                           fontrenderer.drawLines(gamecanvas.screen, strings[gamecanvas.debugEditorValues[0]], 10, 15, 20);
                           break;
                        case 1:
                           s = "Tips";
                           loadTip(gamecanvas.debugEditorValues[1]);
                           gamecanvas.paintTip();
                           break;
                        default:
                           s = "";
                     }

                     fontrenderer.unusedSpacing = 2;
                     fontrenderer.drawString(gamecanvas.screen, s, 40, 228, 20);
                     fontrenderer.drawString(gamecanvas.screen, "" + gamecanvas.debugEditorValues[gamecanvas.debugEditorRow], 120, 228, 20);
                     fontrenderer.drawLines(gamecanvas.screen, "Use up, down, left and right", 10, 190, 20);
                     fontrenderer.drawLines(gamecanvas.screen, "to navigate", 10, 205, 20);
                     gamecanvas.drawSelectSoftKey();
                     gamecanvas.drawBackSoftKey();
               }

               GameCanvas gamecanvas8 = this;
               FontRenderer fontrenderer2 = fontSmall;
               if (noticeMillis > 0) {
                  fontrenderer2.measure(gamecanvas8.noticeText);
                  int i3 = FontRenderer.measuredHeight;
                  int j6 = FontRenderer.measuredWidth;
                  if (gamecanvas8.noticeX == -1) {
                     gamecanvas8.noticeX = 320 - j6 >> 1;
                  }

                  if (gamecanvas8.noticeY == -1) {
                     gamecanvas8.noticeY = 240 - i3 >> 1;
                  }

                  gamecanvas8.screen.setClip(gamecanvas8.noticeX - 6, gamecanvas8.noticeY - 3, j6 + 12, i3 + 26 + 6);
                  drawFramedBox(
                     gamecanvas8.screen, gamecanvas8.noticeX - 6, gamecanvas8.noticeY - 3, j6 + 12, i3 + 6, gamecanvas8.noticeColor, gamecanvas8.noticeStyle
                  );
                  fontrenderer2.drawLines(gamecanvas8.screen, gamecanvas8.noticeText, gamecanvas8.noticeX, gamecanvas8.noticeY + 26, 0);
               }
            } catch (Throwable throwable) {
            }
         }
      } catch (Exception exception2) {
      }
   }

   private static String wrapForScreen(String var0) {
      String[] astring = Cutscene.wrapText(var0, 300);
      String s = "";

      for (int i = 0; i < astring.length; i++) {
         s = s + astring[i] + "\n";
      }

      return s;
   }

   private void drawSealBackground(Graphics var1, boolean var2, int var3, int var4) {
      var1.setClip(0, 0, 320, 240);
      int b0 = 0;
      int b1 = 0;
      if (var3 != 0 || var4 != 0) {
         b0 -= 24;
         b1 -= 24;
      }

      for (int b4 = b1; b4 < 240; b4 += 24) {
         for (int b2 = b0; b2 < 320; b2 += 24) {
            var1.drawImage(imageSets[8][0], b2 + var3, b4 + var4, 0);
         }
      }

      sprites[10].paintFrame(var1, 0, this.sealX + var3, this.sealY + var4, 0, 0, 0);
      if (var2) {
         for (int b5 = 0; b5 < 240; b5 += 24) {
            for (int b3 = 0; b3 < 320; b3 += 24) {
               sprites[17].paintFrame(var1, 16, b3, b5, 0, 0, 0);
            }
         }
      }
   }

   private void drawSealBackground(Graphics var1, boolean var2) {
      this.drawSealBackground(var1, var2, 0, 0);
   }

   private void paintEnding() {
      if (this.fullRedraw) {
         this.drawSealBackground(this.screen, false, this.endingShakeX, this.endingShakeY);
         sprites[17]
            .paintFrame(
               this.screen,
               11,
               160 + Constants.endingCrystalOffsets[6] + this.endingShakeX,
               96 + Constants.endingCrystalOffsets[7] + this.endingShakeY,
               0,
               0,
               0
            );

         for (int j = 0; j < 3; j++) {
            int i = j + 52;
            sprites[i]
               .paintFrame(
                  this.screen,
                  0,
                  Constants.endingCrystalOffsets[j << 1] + 160 - this.crystalHalfWidth + this.endingShakeX,
                  Constants.endingCrystalOffsets[(j << 1) + 1] + 96 - this.crystalHalfHeight + this.endingShakeY,
                  0,
                  0,
                  0
               );
         }
      }

      switch (this.endingPhase) {
         case 1:
            if (!this.fullRedraw) {
               this.screen.setColor(this.endingFlashColor);
               this.screen.fillRect(0, 0, 320, 240);

               for (int l = 0; l < 3; l++) {
                  int k = l + 52;
                  sprites[k]
                     .paintFrame(
                        this.screen,
                        0,
                        Constants.endingCrystalOffsets[l << 1] + 160 - this.crystalHalfWidth,
                        Constants.endingCrystalOffsets[(l << 1) + 1] + 96 - this.crystalHalfHeight,
                        0,
                        0,
                        0
                     );
               }
            }
            break;
         case 2:
            return;
         case 3:
            return;
         case 4:
            return;
         case 5:
            return;
         case 6:
            return;
         case 7:
            return;
         case 8:
            TreasureRain.paint(this.screen);
            return;
         case 9:
         case 10:
            TreasureRain.paint(this.screen);
            String s = wrapForScreen(strings[15] + "\n" + strings[20]);
            fontSmall.measure(s);
            drawFramedBox(
               this.screen,
               (320 - FontRenderer.measuredWidth >> 1) - 3,
               (240 - FontRenderer.measuredHeight >> 1) - 3,
               FontRenderer.measuredWidth + 6,
               FontRenderer.measuredHeight + 6,
               7096587,
               0
            );
            fontSmall.drawLines(this.screen, s, 160, (240 - FontRenderer.measuredHeight >> 1) + 24, 17);
      }
   }

   private void paintSeal() {
      if (!this.fullRedraw && !this.sealDirty) {
         this.screen.setClip(this.sealCursorPrevX + 160, this.sealCursorPrevY + 96, 14, 22);
         sprites[10].paintFrame(this.screen, 0, this.sealX, this.sealY, 0, 0, 0);
      } else {
         this.screen.setClip(0, 0, 320, 240);
         this.drawSealBackground(this.screen, false);
         sprites[17].paintFrame(this.screen, 11, 160 + Constants.endingCrystalOffsets[6], 96 + Constants.endingCrystalOffsets[7] - 20, 0, 0, 0);
         this.drawSelectSoftKey();
         this.drawBackSoftKey();
         fontSmall.drawString(this.screen, strings[36], 298, 234, 10);
         this.fullRedraw = false;
      }

      if (this.crystalsPlaced != 0) {
         this.sealDirty = false;
         this.fullRedraw = true;

         for (int i = 0; i < this.crystalsPlaced; i++) {
            if (isWorldUnlocked(i)) {
               sprites[10].paintFrame(this.screen, i + 1, this.sealX, this.sealY, 0, 0, 0);
            }
         }

         this.screen.setClip(0, 0, 320, 240);
         if (this.crystalAnimFrame < this.crystalAnimLength) {
            sprites[9]
               .paintAnimFrame(
                  this.screen,
                  5,
                  this.crystalAnimFrame,
                  Constants.endingCrystalOffsets[this.crystalsPlaced << 1] + 160 - 12,
                  Constants.endingCrystalOffsets[(this.crystalsPlaced << 1) + 1] + 84,
                  0,
                  0,
                  0
               );
            this.crystalAnimFrame++;
         } else {
            if (this.cursorAnimFrame % this.cursorAnimLength >= this.cursorAnimLength >> 1) {
               sprites[10].paintFrame(this.screen, this.crystalsPlaced + 1, this.sealX, this.sealY, 0, 0, 0);
               this.crystalInsertTicks++;
            }

            if (this.crystalInsertTicks >= 15) {
               crystalInserted[this.crystalsPlaced] = true;
               this.crystalInsertTicks = 0;
               this.crystalsPlaced = 0;
               this.crystalAnimFrame = 0;
               this.updateSealCaption();
               this.sealDirty = true;
               this.fullRedraw = false;
            }
         }
      } else {
         for (int l1 = 0; l1 < 3; l1++) {
            if (crystalInserted[l1]) {
               sprites[10].paintFrame(this.screen, l1 + 1, this.sealX, this.sealY, 0, 0, 0);
            }
         }
      }

      for (int j = 0; j < 3; j++) {
         if (worldCrystalEarned[j]) {
            int i2 = j + 52;
            if (this.flyingCrystal != j) {
               sprites[i2]
                  .paintFrame(
                     this.screen,
                     0,
                     Constants.endingCrystalOffsets[j << 1] + 160 - this.crystalHalfWidth,
                     Constants.endingCrystalOffsets[(j << 1) + 1] + 96 - this.crystalHalfHeight - 40,
                     0,
                     0,
                     0
                  );
            }
         }
      }

      if (this.flyingCrystal == -1) {
         this.cursorAnimFrame = this.cursorAnimFrame % this.cursorAnimLength;
         this.screen.setClip(this.sealCursorX + 160, 96 + this.sealCursorY, 14, 22);
         sprites[55].paintAnimFrame(this.screen, 0, this.cursorAnimFrame, 160 + this.sealCursorX, 96 + this.sealCursorY, 0, 0, 0);
         this.cursorAnimFrame++;
         if (this.sealDirty) {
            String s = Cutscene.wrapText(this.sealCaption, 300).length > 1
               ? wrapForScreen(this.sealCaption + "\n" + this.sealCaptionTarget)
               : wrapForScreen(this.sealCaption) + this.sealCaptionTarget;
            fontSmall.measure(s);
            int k2 = (320 - FontRenderer.measuredWidth >> 1) - 3;
            int l2 = 170 - (fontSmall.font.getHeight() >> 1) - 3;
            int i3 = FontRenderer.measuredWidth + 6;
            int j3 = FontRenderer.measuredHeight + 3;
            this.screen.setClip(k2, l2 - 15, i3, j3 + 20);
            drawFramedBox(this.screen, k2, l2 - 15, i3, j3, 7096587, 0);
            fontSmall.drawLines(this.screen, s, 160, 170, 3);
            this.sealDirty = false;
         }
      } else {
         int k;
         int l;
         int i1;
         int k1;
         GameCanvas gamecanvas;
         int j2;
         label111: {
            this.fullRedraw = true;
            j2 = this.flyingCrystal;
            gamecanvas = this;
            k = j2 + 52;
            l = Constants.endingCrystalOffsets[j2 << 1] + 160;
            int j1 = (i1 = Constants.endingCrystalOffsets[(j2 << 1) + 1] + 96 - 40) - gamecanvas.flyingCrystalY;
            k1 = l - gamecanvas.flyingCrystalX;
            if (j1 < 0) {
               gamecanvas.flyingCrystalY -= 2;
               if (gamecanvas.flyingCrystalY > i1) {
                  break label111;
               }
            } else {
               if (j1 <= 0) {
                  break label111;
               }

               gamecanvas.flyingCrystalY += 2;
               if (gamecanvas.flyingCrystalY < i1) {
                  break label111;
               }
            }

            gamecanvas.flyingCrystalY = i1;
         }

         label103: {
            if (k1 < 0) {
               gamecanvas.flyingCrystalX -= 3;
               if (gamecanvas.flyingCrystalX > l) {
                  break label103;
               }
            } else {
               if (k1 <= 0) {
                  break label103;
               }

               gamecanvas.flyingCrystalX += 3;
               if (gamecanvas.flyingCrystalX < l) {
                  break label103;
               }
            }

            gamecanvas.flyingCrystalX = l;
         }

         boolean flag;
         label95: {
            sprites[k]
               .paintFrame(
                  gamecanvas.screen,
                  0,
                  gamecanvas.flyingCrystalX - gamecanvas.crystalHalfWidth,
                  gamecanvas.flyingCrystalY - gamecanvas.crystalHalfHeight,
                  0,
                  0,
                  0
               );
            if (gamecanvas.flyingCrystalX == l && gamecanvas.flyingCrystalY == i1) {
               gamecanvas.flyingCrystalX = l;
               gamecanvas.flyingCrystalY = i1;
               if (gamecanvas.sealFlashStep < 20) {
                  if (gamecanvas.sealFlashStep % 2 == 1) {
                     j2 = 838860;
                     gamecanvas.screen.setColor(0 + j2 * gamecanvas.sealFlashStep);
                     gamecanvas.screen.fillRect(0, 0, 320, 240);
                  }

                  gamecanvas.sealFlashStep++;
               } else {
                  if (gamecanvas.crystalAnimFrame >= gamecanvas.crystalAnimLength) {
                     gamecanvas.crystalAnimFrame = 0;
                     gamecanvas.sealFlashStep = 0;
                     flag = true;
                     break label95;
                  }

                  sprites[9]
                     .paintAnimFrame(
                        gamecanvas.screen,
                        5,
                        gamecanvas.crystalAnimFrame,
                        Constants.endingCrystalOffsets[j2 << 1] + 160 - 12,
                        Constants.endingCrystalOffsets[(j2 << 1) + 1] + 84 - 40,
                        0,
                        0,
                        0
                     );
                  gamecanvas.crystalAnimFrame++;
               }
            }

            flag = false;
         }

         if (flag) {
            this.flyingCrystal = -1;
            if (worldCrystalEarned[0] && worldCrystalEarned[1] && worldCrystalEarned[2]) {
               this.resetStateTimer();
               this.fullRedraw = true;
               this.endingReached = true;
               gameState = 29;
            }
         }
      }
   }

   private static void drawSplash(Graphics var0, boolean var1) {
      var0.drawImage(splashMiddle, 0, 0, 20);
      var0.drawImage(splashTop, 0, 0, 20);
      if (var1) {
         var0.drawImage(splashBottom, 160, 239, 33);
      }
   }

   private static void blitBackgroundBuffer(Graphics var0, int var1, int var2, int var3, int var4) {
      var0.drawImage(backgroundBuffer, var3 - var1, var4 - var2, 0);
   }

   private void refreshBackgroundTile(int var1, int var2) {
      int i = this.cameraX - this.cameraX % 24;
      int j = this.cameraY - this.cameraY % 24;
      int k = this.cameraX + backgroundBufferWidth - 24 - (this.cameraX + backgroundBufferWidth - 24) % 24;
      int l = this.cameraY + backgroundBufferHeight - 24 - (this.cameraY + backgroundBufferHeight - 24) % 24;
      int i1 = var1 * 24;
      int j1 = var2 * 24;
      if (i1 >= i && i1 <= k && j1 >= j && j1 <= l) {
         i = tileMap[var1][var2] & 0xFF;
         if (objectMap[var1][var2] < 80) {
            if (i == 4 || i == 16 || i == 15) {
               this.redrawTile(var1, var2);
               return;
            }

            backgroundBufferGraphics.drawImage(imageSets[8][0], i1 % backgroundBufferWidth, j1 % backgroundBufferHeight, 0);
         }
      }
   }

   private void redrawTile(int var1, int var2) {
      int i = this.cameraX - this.cameraX % 24;
      int j = this.cameraY - this.cameraY % 24;
      int k = this.cameraX + backgroundBufferWidth - 24 - (this.cameraX + backgroundBufferWidth - 24) % 24;
      int l = this.cameraY + backgroundBufferHeight - 24 - (this.cameraY + backgroundBufferHeight - 24) % 24;
      var1 *= 24;
      var2 *= 24;
      if (var1 >= i && var1 <= k && var2 >= j && var2 <= l) {
         this.renderBackgroundArea(var1, var2, var1, var2, false);
      }
   }

   public static int themedSpriteIndex(int var0) {
      switch (var0) {
         case 0:
            return 42;
         case 1:
            return 60;
         case 2:
            sprites[59].currentPalette = 0;
            return 59;
         case 3:
            sprites[59].currentPalette = 1;
            return 59;
         case 4:
            return 56;
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 17:
         case 18:
         case 19:
         case 21:
         default:
            return -1;
         case 14:
            sprites[57].currentPalette = 1;
            return 57;
         case 15:
            sprites[57].currentPalette = 0;
            return 57;
         case 16:
            return 58;
         case 20:
            return 43;
         case 22:
            sprites[45].currentPalette = 0;
            return 45;
         case 23:
            sprites[45].currentPalette = 1;
            return 45;
      }
   }

   private void renderBackgroundArea(int var1, int var2, int var3, int var4, boolean var5) {
      Graphics graphics1 = backgroundBufferGraphics;
      int i = backgroundBufferWidth;
      int j = backgroundBufferHeight;
      int k = var1 % i;
      int l = var2 % j;
      this.drawY = l - 24;
      this.paintTileY = var2 / 24 - 1;

      for (int i1 = var2; i1 <= var4; i1 += 24) {
         this.drawX = k - 24;
         this.paintTileX = var1 / 24 - 1;
         this.drawY += 24;
         this.paintTileY++;
         if (this.drawY >= j) {
            this.drawY = 0;
         }

         for (int j1 = var1; j1 <= var3; j1 += 24) {
            this.drawX += 24;
            this.paintTileX++;
            if (this.drawX >= i) {
               this.drawX = 0;
            }

            if (this.paintTileX >= 0 && this.paintTileX < this.mapWidth && this.paintTileY >= 0 && this.paintTileY < this.mapHeight) {
               this.paintObject = objectMap[this.paintTileX][this.paintTileY];
               this.paintTerrain = tileMap[this.paintTileX][this.paintTileY] & 0xFF;
               if (this.paintObject < 80) {
                  graphics1.drawImage(imageSets[8][0], this.drawX, this.drawY, 0);
               }

               label131:
               if (this.paintTerrain > -1 && this.paintTerrain < 38) {
                  GameCanvas gamecanvas2;
                  int l1;
                  switch (this.paintTerrain) {
                     case 4:
                        this.paintImageSet = 20;
                        this.paintFrame = 7;
                        this.paintOffsetX = this.paintOffsetY = 0;
                        break label131;
                     case 15:
                        if (this.togglePhase != 0) {
                           break label131;
                        }

                        this.paintImageSet = 14;
                        gamecanvas2 = this;
                        l1 = 0 + this.togglePhase * 5 / 10;
                        break;
                     case 16:
                        if (this.togglePhase != 9) {
                           break label131;
                        }

                        this.paintImageSet = 15;
                        this.paintFrame = 4 - this.togglePhase * 5 / 10;
                        if (this.paintFrame >= 0) {
                           break label131;
                        }

                        gamecanvas2 = this;
                        l1 = 0;
                        break;
                     case 27:
                        this.paintImageSet = 21;
                        this.paintFrame = 0;
                        this.paintOffsetX = this.paintOffsetY = 0;
                     default:
                        break label131;
                  }

                  gamecanvas2.paintFrame = l1;
               }

               if (this.paintImageSet != -1) {
                  if (imageSets[this.paintImageSet] == null) {
                     sprites[themedSpriteIndex(this.paintImageSet)]
                        .paintFrame(graphics1, this.paintFrame, this.drawX + this.paintOffsetX, this.drawY + this.paintOffsetY, this.paintTransform, 0, 0);
                  } else {
                     graphics1.drawImage(
                        imageSets[this.paintImageSet][this.paintFrame], this.drawX + this.paintOffsetX, this.drawY + this.paintOffsetY, this.paintTransform
                     );
                  }

                  this.paintTransform = 0;
                  this.paintImageSet = -1;
                  this.paintOffsetX = this.paintOffsetY = 0;
               }

               if (this.paintObject != -1) {
                  label114: {
                     byte b0;
                     GameCanvas gamecanvas1;
                     int k1;
                     if ((b0 = (byte)(this.paintObject - 80)) >= 0) {
                        this.paintOverlaySet = 0;
                        gamecanvas1 = this;
                        k1 = b0;
                     } else {
                        switch (this.paintObject) {
                           case 0:
                              if (objectTimers[this.paintTileX][this.paintTileY] == 0
                                 && (tileSlots == null || tileSlots[this.paintTileX][this.paintTileY] == 0)) {
                                 this.computeObjectOffset();
                              }
                              break label114;
                           case 1:
                              if (objectTimers[this.paintTileX][this.paintTileY] == 0
                                 && (tileSlots == null || tileSlots[this.paintTileX][this.paintTileY] == 0)) {
                                 this.computeObjectOffset();
                                 this.paintOverlayFrame = this.paintOverlayFrame - this.paintAnimPhase;
                                 this.paintOffsetY = 0;
                              }
                              break label114;
                           case 10:
                              sprites[16].paintFrame(graphics1, 0, this.drawX, this.drawY, 0, 0, 0);
                              break label114;
                           case 34:
                              if (this.togglePhase != 9) {
                                 break label114;
                              }

                              this.paintOverlaySet = 14;
                              gamecanvas1 = this;
                              k1 = 0 + this.togglePhase * 5 / 10;
                              break;
                           case 35:
                              if (this.togglePhase == 0) {
                                 this.paintOverlaySet = 15;
                                 this.paintOverlayFrame = 4 - this.togglePhase * 5 / 10;
                                 if (this.paintOverlayFrame < 0) {
                                    gamecanvas1 = this;
                                    k1 = 0;
                                    break;
                                 }
                              }
                           default:
                              break label114;
                        }
                     }

                     gamecanvas1.paintOverlayFrame = k1;
                  }

                  if (this.paintOverlaySet != -1) {
                     if (imageSets[this.paintOverlaySet] == null) {
                        sprites[themedSpriteIndex(this.paintOverlaySet)]
                           .paintFrame(
                              graphics1, this.paintOverlayFrame, this.drawX + this.paintOffsetX, this.drawY + this.paintOffsetY, this.paintTransform, 0, 0
                           );
                     } else {
                        graphics1.drawImage(
                           imageSets[this.paintOverlaySet][this.paintOverlayFrame],
                           this.drawX + this.paintOffsetX,
                           this.drawY + this.paintOffsetY,
                           this.paintTransform
                        );
                     }

                     this.paintOverlaySet = -1;
                     this.paintTransform = 0;
                     this.paintOffsetX = this.paintOffsetY = 0;
                  }
               }

               if (this.stageKind == 2) {
                  Graphics graphics = graphics1;
                  GameCanvas gamecanvas = this;
                  if (sprites[10] == null) {
                     byte b1 = 0;
                     String s = "/mmv.f";
                     boolean flag = false;
                     b1 = b1;
                     s = "/mmv.f";
                     sprites[10] = loadSprite("/mmv.f", b1, 0, 0);
                  }

                  if (gamecanvas.paintTileX >= 60 && gamecanvas.paintTileX < 65 && gamecanvas.paintTileY >= 2 && gamecanvas.paintTileY < 7) {
                     sprites[10]
                        .paintFrame(graphics, 4 + (gamecanvas.paintTileY - 2) * 5 + gamecanvas.paintTileX - 60, gamecanvas.drawX, gamecanvas.drawY, 0, 0, 0);
                  }
               }
            }
         }
      }
   }

   private void clipToPlayfield() {
      this.screen.setClip(0, 0, 320, 178);
   }

   private void paintPlaying() {
      Graphics graphics = this.screen;
      int i = stateTicks;
      this.paintPlayerDirection = (this.playerFlags & 4096) == 0 ? this.queuedAction : 0;
      graphics.translate(0, 31);
      this.screenShakeOffset = 0;
      this.clipToPlayfield();
      if (this.screenShakeTicks > 0) {
         this.screenShakeOffset = this.screenShakeTicks * i % ((this.screenShakeTicks >> 1) + 1) % 12;
      }

      if (this.screenShakeOffset > this.cameraY) {
         this.screenShakeOffset = this.cameraY;
      }

      this.cameraY = this.cameraY - this.screenShakeOffset;
      this.cameraTileX = this.cameraX / 24;
      this.cameraTileY = this.cameraY / 24;
      this.cameraTileOffsetX = this.cameraX % 24;
      this.cameraTileOffsetY = this.cameraY % 24;
      this.paintAnimPhase = (i & 63) >> 1;
      if (this.paintAnimPhase >= 4) {
         this.paintAnimPhase = 0;
      }

      int j = this.cameraX - this.cameraTileOffsetX;
      int k = this.cameraY - this.cameraTileOffsetY;
      int l = this.cameraX + backgroundBufferWidth - 24 - (this.cameraX + backgroundBufferWidth - 24) % 24;
      int i1 = this.cameraY + backgroundBufferHeight - 24 - (this.cameraY + backgroundBufferHeight - 24) % 24;
      if (bufferedLeft == -1) {
         this.renderBackgroundArea(j, k, l, i1, false);
         bufferedLeft = j;
         bufferedRight = l;
         bufferedTop = k;
         bufferedBottom = i1;
      }

      if (bufferedLeft != j) {
         int j1;
         int l17;
         if (bufferedLeft < j) {
            j1 = bufferedRight + 24;
            l17 = l;
         } else {
            j1 = j;
            l17 = bufferedLeft - 24;
         }

         int k1 = l17;
         this.renderBackgroundArea(j1, k, k1, i1, false);
         bufferedLeft = j;
         bufferedRight = l;
      }

      if (bufferedTop != k) {
         int i4;
         int k16;
         if (bufferedTop < k) {
            i4 = bufferedBottom + 24;
            k16 = i1;
         } else {
            i4 = k;
            k16 = bufferedTop - 24;
         }

         int k5 = k16;
         this.renderBackgroundArea(j, i4, l, k5, false);
         bufferedTop = k;
         bufferedBottom = i1;
      }

      Graphics graphics4;
      int i18;
      int j18;
      int k18;
      int l18;
      label854: {
         Graphics graphics1 = graphics;
         GameCanvas gamecanvas = this;
         l = this.cameraX % backgroundBufferWidth;
         i1 = gamecanvas.cameraY % backgroundBufferHeight;
         int j4 = (gamecanvas.cameraX + 320) % backgroundBufferWidth;
         int l5 = (gamecanvas.cameraY + 178) % backgroundBufferHeight;
         if (j4 > l) {
            if (l5 > i1) {
               graphics4 = graphics1;
               i18 = l;
               j18 = i1;
               k18 = 0;
               l18 = 0;
               break label854;
            }

            blitBackgroundBuffer(graphics1, l, i1, 0, 0);
            graphics4 = graphics1;
            i18 = l;
            j18 = 0;
            k18 = 0;
         } else {
            if (l5 > i1) {
               blitBackgroundBuffer(graphics1, l, i1, 0, 0);
               graphics4 = graphics1;
               i18 = 0;
               j18 = i1;
               k18 = 320 - j4;
               l18 = 0;
               break label854;
            }

            blitBackgroundBuffer(graphics1, l, i1, 0, 0);
            blitBackgroundBuffer(graphics1, l, 0, 0, 178 - l5);
            blitBackgroundBuffer(graphics1, 0, i1, 320 - j4, 0);
            graphics4 = graphics1;
            i18 = 0;
            j18 = 0;
            k18 = 320 - j4;
         }

         l18 = 178 - l5;
      }

      blitBackgroundBuffer(graphics4, i18, j18, k18, l18);
      this.clipToPlayfield();
      Graphics graphics2 = graphics;
      GameCanvas gamecanvas1 = this;

      for (int k3 = -1; k3 < 9; k3++) {
         for (int l3 = -1; l3 < 15; l3++) {
            gamecanvas1.paintTileX = l3 + gamecanvas1.cameraTileX;
            gamecanvas1.paintTileY = k3 + gamecanvas1.cameraTileY;
            if (gamecanvas1.paintTileX >= 0
               && gamecanvas1.paintTileX < gamecanvas1.mapWidth
               && gamecanvas1.paintTileY >= 0
               && gamecanvas1.paintTileY < gamecanvas1.mapHeight) {
               gamecanvas1.paintObject = objectMap[gamecanvas1.paintTileX][gamecanvas1.paintTileY];
               gamecanvas1.paintTerrain = tileMap[gamecanvas1.paintTileX][gamecanvas1.paintTileY] & 0xFF;
               gamecanvas1.drawX = l3 * 24 - gamecanvas1.cameraTileOffsetX;
               gamecanvas1.drawY = k3 * 24 - gamecanvas1.cameraTileOffsetY;
               gamecanvas1.clipToPlayfield();
               label839:
               if (gamecanvas1.paintTerrain > -1 && gamecanvas1.paintTerrain < 38) {
                  label836: {
                     GameCanvas gamecanvas27;
                     switch (gamecanvas1.paintTerrain) {
                        case 3:
                           int k4;
                           if ((k4 = (tileMap[gamecanvas1.paintTileX][gamecanvas1.paintTileY] >> 8) - 1) >= 0) {
                              gamecanvas1.paintImageSet = 12;
                              gamecanvas1.paintFrame = (byte)k4;
                           }
                           break label836;
                        case 4:
                           if (tileMap[gamecanvas1.paintTileX][gamecanvas1.paintTileY] >> 8 >= gamecanvas1.nextCompassTarget) {
                              GameCanvas gamecanvas11 = gamecanvas1;
                              gamecanvas1.paintImageSet = 20;
                              GameCanvas gamecanvas31;
                              if (tileMap[gamecanvas11.paintTileX][gamecanvas11.paintTileY] >> 8 >= gamecanvas11.nextCompassTarget) {
                                 gamecanvas31 = gamecanvas11;
                                 i18 = 0 + (stateTicks >> 1) % 7;
                              } else {
                                 gamecanvas31 = gamecanvas11;
                                 i18 = 7;
                              }

                              gamecanvas31.paintFrame = i18;
                           }
                           break label836;
                        case 5:
                           System.out.println("k_AOBJ_STAGE_END ..................");
                        case 28:
                           System.out.println("k_AOBJ_SECRET_EXIT ..................");
                           gamecanvas1.paintImageSet = 11;
                           GameCanvas gamecanvas30;
                           byte b10;
                           if ((gamecanvas1.world != 0 || gamecanvas1.stage != 4 && gamecanvas1.stage != 7)
                              && (gamecanvas1.world != 1 || gamecanvas1.stage != 8)
                              && (
                                 gamecanvas1.world != 2 || gamecanvas1.stage != 1 && gamecanvas1.stage != 2 && gamecanvas1.stage != 6 && gamecanvas1.stage != 7
                              )) {
                              gamecanvas30 = gamecanvas1;
                              b10 = 0;
                           } else {
                              gamecanvas30 = gamecanvas1;
                              b10 = 1;
                           }

                           gamecanvas30.paintFrame = b10;
                           if (gamecanvas1.paintTerrain == 28
                              && (gamecanvas1.world == 1 && gamecanvas1.stage == 3 || gamecanvas1.world == 2 && gamecanvas1.stage == 4)) {
                              gamecanvas1.paintFrame = 1;
                           }
                           break label836;
                        case 6:
                           GameCanvas gamecanvas10;
                           label938: {
                              gamecanvas10 = gamecanvas1;
                              int l6 = gamecanvas1.paintTileX;
                              int l7 = gamecanvas10.paintTileY;
                              gamecanvas10.paintImageSet = 26;
                              gamecanvas10.paintFrame = 0;
                              GameCanvas gamecanvas29;
                              if (isHeavyObject(l6, l7) && objectTimers[l6][l7] <= 12) {
                                 gamecanvas29 = gamecanvas10;
                                 i18 = -(objectTimers[l6][l7] - 12);
                              } else if (gamecanvas10.isPlayerAt(l6, l7)) {
                                 if ((gamecanvas10.playerFlags & 4096) == 0) {
                                    if (gamecanvas10.moveProgress > 12) {
                                       break label938;
                                    }

                                    gamecanvas29 = gamecanvas10;
                                    i18 = -(gamecanvas10.moveProgress - 12);
                                 } else {
                                    gamecanvas29 = gamecanvas10;
                                    i18 = 12;
                                 }
                              } else {
                                 if (gamecanvas10.isPlayerAt(l6 - 1, l7)
                                    ? gamecanvas10.paintPlayerDirection != 4 || gamecanvas10.moveProgress <= 12
                                    : !gamecanvas10.isPlayerAt(l6 + 1, l7) || gamecanvas10.paintPlayerDirection != 2 || gamecanvas10.moveProgress <= 12) {
                                    break label938;
                                 }

                                 gamecanvas29 = gamecanvas10;
                                 i18 = gamecanvas10.moveProgress - 12;
                              }

                              gamecanvas29.paintOffsetY = i18;
                           }

                           gamecanvas10.paintOffsetY += 24;
                           gamecanvas10.paintTransform = 36;
                           if (gamecanvas1.drawY + 24 < 178) {
                              graphics2.clipRect(gamecanvas1.drawX, gamecanvas1.drawY, 24, 24);
                           }
                           break label836;
                        case 7:
                           GameCanvas gamecanvas9 = gamecanvas1;
                           int k6 = gamecanvas1.paintTileX;
                           int k7 = gamecanvas9.paintTileY;
                           int j2;
                           if ((j2 = ((tileMap[k6][k7] >> 8 & 240) >> 4) - 1) < 0) {
                              j2 = 0;
                           }

                           if ((tileMap[k6][k7 - 1] & 0xFF) != 9 && (tileMap[k6][k7 - 1] & 0xFF) != 8) {
                              sprites[56].paintFrame(gamecanvas9.screen, j2, gamecanvas9.drawX, gamecanvas9.drawY, 0, 0, 0);
                           }

                           gamecanvas9.paintImageSet = 4;
                           gamecanvas9.paintFrame = (byte)(j2 + 3);
                           break label836;
                        case 8:
                        case 9:
                           GameCanvas gamecanvas8 = gamecanvas1;
                           int j6 = tileMap[gamecanvas8.paintTileX][gamecanvas8.paintTileY] >> 8;
                           gamecanvas8.paintImageSet = gamecanvas8.paintTerrain == 9 ? 22 : 23;
                           GameCanvas gamecanvas28;
                           byte b9;
                           if ((j6 & 512) != 0) {
                              gamecanvas28 = gamecanvas8;
                              b9 = (byte)(1 + (stateTicks >> 2) % 6);
                           } else if ((j6 & 256) != 0) {
                              gamecanvas28 = gamecanvas8;
                              b9 = 1;
                           } else {
                              gamecanvas28 = gamecanvas8;
                              b9 = 0;
                           }

                           gamecanvas28.paintFrame = b9;
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 29:
                        case 30:
                        case 31:
                        case 32:
                        case 36:
                        default:
                           break label836;
                        case 14:
                        case 33:
                           int i6 = gamecanvas1.paintTerrain;
                           GameCanvas gamecanvas7 = gamecanvas1;
                           int l1;
                           if ((l1 = tileMap[gamecanvas7.paintTileX][gamecanvas7.paintTileY] >> 8) == 255) {
                              l1 = 0;
                           }

                           int i2 = 14 == i6 ? 8 : 22;
                           if (sprites[i2] != null) {
                              sprites[i2].paintAnimFrame(gamecanvas7.screen, 0, l1, gamecanvas7.drawX, gamecanvas7.drawY, 0, 0, 0);
                           }
                           break label836;
                        case 15:
                           if (gamecanvas1.togglePhase != 0 && gamecanvas1.togglePhase <= 5) {
                              gamecanvas1.paintImageSet = 14;
                              gamecanvas1.paintFrame = 0 + gamecanvas1.togglePhase * 5 / 10;
                           }
                           break label836;
                        case 16:
                           if (gamecanvas1.togglePhase != 9 && gamecanvas1.togglePhase >= 5) {
                              gamecanvas1.paintImageSet = 15;
                              gamecanvas1.paintFrame = 4 - gamecanvas1.togglePhase * 5 / 10;
                              if (gamecanvas1.paintFrame < 0) {
                                 gamecanvas1.paintFrame = 0;
                              }
                           }
                           break label836;
                        case 34:
                           sprites[27].paintAnimFrame(graphics2, 2, 0, gamecanvas1.drawX, gamecanvas1.drawY, 0, 0, 0);
                           break label836;
                        case 35:
                           gamecanvas27 = gamecanvas1;
                           break;
                        case 37:
                           sprites[27].paintAnimFrame(graphics2, 2, 0, gamecanvas1.drawX, gamecanvas1.drawY, 0, 0, 0);
                           gamecanvas27 = gamecanvas1;
                     }

                     GameCanvas gamecanvas6 = gamecanvas27;
                     paintSprite = sprites[30];
                     gamecanvas6.paintSpriteAnim = 1;
                     gamecanvas6.paintSpriteFrame = frameAtTime(paintSprite, 1, stateTicks % animLength(paintSprite, 1));
                  }

                  if (gamecanvas1.paintImageSet != -1) {
                     if (imageSets[gamecanvas1.paintImageSet] == null) {
                        sprites[themedSpriteIndex(gamecanvas1.paintImageSet)]
                           .paintFrame(
                              graphics2,
                              gamecanvas1.paintFrame,
                              gamecanvas1.drawX + gamecanvas1.paintOffsetX,
                              gamecanvas1.drawY + gamecanvas1.paintOffsetY,
                              gamecanvas1.paintTransform,
                              0,
                              0
                           );
                     } else {
                        graphics2.drawImage(
                           imageSets[gamecanvas1.paintImageSet][gamecanvas1.paintFrame],
                           gamecanvas1.drawX + gamecanvas1.paintOffsetX,
                           gamecanvas1.drawY + gamecanvas1.paintOffsetY,
                           gamecanvas1.paintTransform
                        );
                     }

                     gamecanvas1.paintTransform = 0;
                     gamecanvas1.paintImageSet = -1;
                     graphics2.setClip(0, -gamecanvas1.screenShakeOffset, 320, 178);
                  } else {
                     if (paintSprite == null) {
                        break label839;
                     }

                     if (gamecanvas1.paintSpriteAnim != -1) {
                        paintSprite.paintAnimFrame(
                           graphics2,
                           gamecanvas1.paintSpriteAnim,
                           gamecanvas1.paintSpriteFrame,
                           gamecanvas1.drawX + gamecanvas1.paintOffsetX,
                           gamecanvas1.drawY + gamecanvas1.paintOffsetY,
                           0,
                           0,
                           0
                        );
                        gamecanvas1.paintSpriteAnim = -1;
                     }

                     paintSprite = null;
                  }

                  gamecanvas1.paintOffsetX = gamecanvas1.paintOffsetY = 0;
               }

               if (gamecanvas1.paintObject != -1) {
                  label773:
                  if ((byte)(gamecanvas1.paintObject - 80) < 0) {
                     int i7 = objectState[gamecanvas1.paintTileX][gamecanvas1.paintTileY];
                     GameCanvas gamecanvas35;
                     switch (gamecanvas1.paintObject) {
                        case 0:
                           if ((objectState[gamecanvas1.paintTileX][gamecanvas1.paintTileY] & -2147483648) == 0) {
                              break label773;
                           }
                        case 8:
                           gamecanvas35 = gamecanvas1;
                           break;
                        case 1:
                           if ((objectState[gamecanvas1.paintTileX][gamecanvas1.paintTileY] & -2147483648) != 0 || gamecanvas1.paintAnimPhase != 0) {
                              gamecanvas35 = gamecanvas1;
                              break;
                           }
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 10:
                        case 13:
                        case 15:
                        case 17:
                        case 20:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 29:
                        case 31:
                        case 33:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        default:
                           break label773;
                        case 9:
                           gamecanvas1.computeObjectOffset();
                           if ((objectState[gamecanvas1.paintTileX][gamecanvas1.paintTileY] & 264241152) == 138412032) {
                              gamecanvas1.paintOffsetX = gamecanvas1.paintOffsetX + gamecanvas1.shakeX;
                              gamecanvas1.paintOffsetY = gamecanvas1.paintOffsetY + gamecanvas1.shakeY;
                           }
                           break label773;
                        case 11:
                           GameCanvas gamecanvas25 = gamecanvas1;
                           i7 = gamecanvas1.paintTileX;
                           int i10 = gamecanvas25.paintTileY;
                           gamecanvas25.paintOverlaySet = 6;
                           int i12;
                           if ((i12 = (objectState[i7][i10] & 3840) >> 8) >= 4) {
                              gamecanvas25.paintOverlaySet = -1;
                           } else {
                              GameCanvas gamecanvas43;
                              if (i12 == 0) {
                                 gamecanvas43 = gamecanvas25;
                                 i18 = 0 + (stateTicks >> 1) % 3;
                              } else {
                                 gamecanvas43 = gamecanvas25;
                                 i18 = i12 + 3 - 1;
                              }

                              gamecanvas43.paintOverlayFrame = i18;
                              int l13 = objectState[i7][i10] & 7;
                              gamecanvas25.paintOffsetX = objectTimers[i7][i10] * directionOffsets[l13] + 2;
                              gamecanvas25.paintOffsetY = objectTimers[i7][i10] * directionOffsets[l13 + 8] + 2;
                              if ((objectState[i7][i10] & 16) == 0) {
                                 GameCanvas gamecanvas44;
                                 switch (l13) {
                                    case 1:
                                       gamecanvas25.paintOffsetX += 4;
                                       break label773;
                                    case 2:
                                       gamecanvas44 = gamecanvas25;
                                       i18 = gamecanvas25.paintOffsetY + 4;
                                       break;
                                    case 3:
                                       gamecanvas25.paintOffsetX -= 4;
                                       break label773;
                                    case 4:
                                       gamecanvas44 = gamecanvas25;
                                       i18 = gamecanvas25.paintOffsetY - 4;
                                       break;
                                    default:
                                       break label773;
                                 }

                                 gamecanvas44.paintOffsetY = i18;
                              } else {
                                 GameCanvas gamecanvas45;
                                 switch (l13) {
                                    case 1:
                                       gamecanvas25.paintOffsetX -= 4;
                                       break label773;
                                    case 2:
                                       gamecanvas45 = gamecanvas25;
                                       i18 = gamecanvas25.paintOffsetY - 4;
                                       break;
                                    case 3:
                                       gamecanvas25.paintOffsetX += 4;
                                       break label773;
                                    case 4:
                                       gamecanvas45 = gamecanvas25;
                                       i18 = gamecanvas25.paintOffsetY + 4;
                                       break;
                                    default:
                                       break label773;
                                 }

                                 gamecanvas45.paintOffsetY = i18;
                              }
                           }
                           break label773;
                        case 12:
                           GameCanvas gamecanvas24 = gamecanvas1;
                           boolean flag1 = false;
                           int l11 = gamecanvas24.drawY;
                           int l9 = gamecanvas24.drawX;
                           GameCanvas gamecanvas26 = gamecanvas1;
                           Image[] aimage = imageSets[18];
                           gamecanvas26.screen.drawImage(aimage[1], l9 + 6, l11, 0);
                           gamecanvas26.screen.drawImage(aimage[0], l9 + 3, l11 + 7, 0);
                           int l4 = 0;
                           if (gamecanvas26.padlockGemsRemaining < 10) {
                              l4 = sprites[0].moduleImages[0][0].getWidth() >> 1;
                              l4++;
                           }

                           drawNumber(gamecanvas26.screen, l9 + 19 - l4, l11 + 13, gamecanvas26.padlockGemsRemaining, sprites[0].moduleImages[0], 0);
                           break label773;
                        case 14:
                           GameCanvas gamecanvas23 = gamecanvas1;
                           i7 = stateTicks;
                           int k9 = objectState[gamecanvas23.paintTileX][gamecanvas23.paintTileY];
                           byte b6;
                           if ((b6 = objectTimers[gamecanvas23.paintTileX][gamecanvas23.paintTileY]) > 24) {
                              b6 = 24;
                           }

                           int k13 = k9 & 7;
                           gamecanvas23.paintOffsetX = b6 * directionOffsets[k13];
                           gamecanvas23.paintOffsetY = b6 * directionOffsets[k13 + 8];
                           if ((k9 & 8) == 0) {
                              gamecanvas23.paintOverlaySet = 10;
                              gamecanvas23.paintOverlayFrame = (i7 >> 1) % 3;
                              if (k13 != 3) {
                                 int l14 = (i7 >> 1) % 5;
                                 gamecanvas23.screen
                                    .drawImage(
                                       imageSets[10][l14 + 3],
                                       gamecanvas23.drawX + gamecanvas23.paintOffsetX - (l14 << 2),
                                       gamecanvas23.drawY + gamecanvas23.paintOffsetY + 24,
                                       36
                                    );
                              }
                           } else {
                              gamecanvas23.paintOverlaySet = 10;
                              gamecanvas23.paintOverlayFrame = 2 - (i7 >> 1) % 3;
                              if (k13 != 3) {
                                 int i15 = (i7 >> 1) % 5;
                                 gamecanvas23.screen
                                    .drawImage(
                                       imageSets[10][i15 + 8],
                                       gamecanvas23.drawX + 24 - 12 + gamecanvas23.paintOffsetX + i15 * 3,
                                       gamecanvas23.drawY + gamecanvas23.paintOffsetY + 24,
                                       36
                                    );
                                 if ((i7 >> 1 & 1) == 0 && objectMap[gamecanvas23.paintTileX - 1][gamecanvas23.paintTileY] >= 0) {
                                    gamecanvas23.paintOffsetX--;
                                    gamecanvas23.paintOffsetY++;
                                 }
                              }
                           }
                           break label773;
                        case 16:
                           GameCanvas gamecanvas22 = gamecanvas1;
                           if (objectMap[gamecanvas22.paintTileX][gamecanvas22.paintTileY + 1] != 16) {
                              i7 = objectState[gamecanvas22.paintTileX][gamecanvas22.paintTileY];
                              Sprite sprite4;
                              paintSprite = sprite4 = sprites[1];
                              byte b5 = objectTimers[gamecanvas22.paintTileX][gamecanvas22.paintTileY];
                              int j13 = 36 - b5;
                              int k14 = 0;
                              gamecanvas22.paintSpriteAnim = (i7 & 7) == 4 ? 1 : 0;
                              if (b5 != 0) {
                                 for (int i16 = 0; j13 > 0; k14 = i16++) {
                                    j13 -= sprite4.getAnimFrameDuration(gamecanvas22.paintSpriteAnim, i16);
                                 }
                              }

                              gamecanvas22.paintSpriteFrame = k14;
                              int j16 = (sprite4.animFrameStarts[gamecanvas22.paintSpriteAnim] + k14) * 5;
                              gamecanvas22.paintOffsetX = sprite4.animFrames[j16 + 2];
                           }
                           break label773;
                        case 18:
                           GameCanvas gamecanvas21 = gamecanvas1;
                           paintSprite = sprites[6];
                           gamecanvas21.paintSpriteAnim = 0;
                           GameCanvas gamecanvas42;
                           byte b14;
                           if (gamecanvas21.togglePhase == 0) {
                              gamecanvas42 = gamecanvas21;
                              b14 = 0;
                           } else if (gamecanvas21.togglePhase == 9) {
                              gamecanvas42 = gamecanvas21;
                              b14 = 2;
                           } else {
                              gamecanvas42 = gamecanvas21;
                              b14 = (byte)(gamecanvas21.toggleDirection < 0 ? 1 : 3);
                           }

                           gamecanvas42.paintSpriteFrame = b14;
                           break label773;
                        case 19:
                           gamecanvas1.selectRollingObjectSprite((byte)gamecanvas1.paintObject);
                           break label773;
                        case 21:
                           GameCanvas gamecanvas20 = gamecanvas1;
                           i7 = objectState[gamecanvas20.paintTileX][gamecanvas20.paintTileY] & 7;
                           paintSprite = sprites[29];
                           GameCanvas gamecanvas40;
                           if ((objectState[gamecanvas20.paintTileX][gamecanvas20.paintTileY] & 8) == 0) {
                              label716: {
                                 GameCanvas gamecanvas41;
                                 byte b13;
                                 switch (i7) {
                                    case 1:
                                       gamecanvas41 = gamecanvas20;
                                       b13 = 2;
                                       break;
                                    case 2:
                                       gamecanvas41 = gamecanvas20;
                                       b13 = 1;
                                       break;
                                    case 3:
                                    default:
                                       break label716;
                                    case 4:
                                       gamecanvas41 = gamecanvas20;
                                       b13 = 0;
                                 }

                                 gamecanvas41.paintSpriteFrame = b13;
                              }

                              gamecanvas20.paintOffsetX = objectTimers[gamecanvas20.paintTileX][gamecanvas20.paintTileY] * directionOffsets[i7];
                              gamecanvas40 = gamecanvas20;
                              i18 = objectTimers[gamecanvas20.paintTileX][gamecanvas20.paintTileY] * directionOffsets[i7 + 8];
                           } else {
                              label722: {
                                 GameCanvas gamecanvas39;
                                 byte b12;
                                 switch (i7) {
                                    case 1:
                                       gamecanvas39 = gamecanvas20;
                                       b12 = 14;
                                       break;
                                    case 2:
                                       gamecanvas39 = gamecanvas20;
                                       b12 = 13;
                                       break;
                                    case 3:
                                    default:
                                       break label722;
                                    case 4:
                                       gamecanvas39 = gamecanvas20;
                                       b12 = 12;
                                 }

                                 gamecanvas39.paintSpriteAnim = b12;
                              }

                              gamecanvas20.paintSpriteFrame = frameAtTime(
                                 paintSprite, gamecanvas20.paintSpriteAnim, objectTimers[gamecanvas20.paintTileX][gamecanvas20.paintTileY]
                              );
                              int j9 = (paintSprite.animFrameStarts[gamecanvas20.paintSpriteAnim] + gamecanvas20.paintSpriteFrame) * 5;
                              gamecanvas20.paintOffsetX = paintSprite.animFrames[j9 + 2];
                              gamecanvas40 = gamecanvas20;
                              i18 = paintSprite.animFrames[j9 + 3];
                           }

                           gamecanvas40.paintOffsetY = i18;
                           break label773;
                        case 22:
                           paintSprite = sprites[12];
                           gamecanvas1.paintSpriteFrame = 1;
                           gamecanvas1.paintSideObject((byte)22);
                           break label773;
                        case 23:
                           paintSprite = sprites[12];
                           gamecanvas1.paintSpriteFrame = 0;
                           gamecanvas1.paintSideObject((byte)23);
                           break label773;
                        case 28:
                           GameCanvas gamecanvas19 = gamecanvas1;
                           byte b2 = -1;
                           byte b3 = 3;
                           int k11;
                           if (((k11 = objectState[gamecanvas19.paintTileX][gamecanvas19.paintTileY]) & 7) == 3) {
                              b2 = 1;
                              b3 = 0;
                           }

                           int i13;
                           int i17;
                           if ((k11 & 8) == 0) {
                              i13 = gamecanvas19.trapReachA;
                              i17 = gamecanvas19.trapLengthA;
                           } else {
                              i13 = gamecanvas19.trapReachB;
                              i17 = gamecanvas19.trapLengthB;
                           }

                           int j14 = i17;

                           for (int l15 = 0; l15 < i13; l15++) {
                              sprites[11]
                                 .paintFrame(gamecanvas19.screen, b3 + l15 * b2, gamecanvas19.drawX + 3, gamecanvas19.drawY + b2 * (j14 - l15 * 24), 0, 0, 0);
                           }

                           Sprite sprite6;
                           Graphics graphics5;
                           if (b2 == 1) {
                              sprite6 = sprites[42];
                              graphics5 = gamecanvas19.screen;
                              j18 = objectMap[gamecanvas19.paintTileX][gamecanvas19.paintTileY - 1] - 80;
                              k18 = gamecanvas19.drawX;
                              l18 = gamecanvas19.drawY - 24;
                           } else {
                              sprite6 = sprites[42];
                              graphics5 = gamecanvas19.screen;
                              j18 = objectMap[gamecanvas19.paintTileX][gamecanvas19.paintTileY + 1] - 80;
                              k18 = gamecanvas19.drawX;
                              l18 = gamecanvas19.drawY + 24;
                           }

                           sprite6.paintFrame(graphics5, j18, k18, l18, 0, 0, 0);
                           break label773;
                        case 30:
                           paintSprite = sprites[20];
                           gamecanvas1.paintSpriteAnim = 0;
                           gamecanvas1.paintSpriteFrame = 0 + (i7 - 1) * 7 / 16;
                           break label773;
                        case 32:
                           GameCanvas gamecanvas18 = gamecanvas1;
                           gamecanvas1.screen.setColor(13883367);
                           byte b4 = objectTimers[gamecanvas18.paintTileX][gamecanvas18.paintTileY];
                           int i9;
                           GameCanvas gamecanvas38;
                           byte b11;
                           if ((objectState[gamecanvas18.paintTileX][gamecanvas18.paintTileY] & 1) != 0) {
                              i7 = gamecanvas18.drawX;
                              i9 = gamecanvas18.drawX + 24 - b4;
                              gamecanvas38 = gamecanvas18;
                              b11 = 0;
                           } else {
                              i7 = gamecanvas18.drawX + 24;
                              i9 = gamecanvas18.drawX + b4;
                              gamecanvas38 = gamecanvas18;
                              b11 = 1;
                           }

                           gamecanvas38.paintSpriteFrame = b11;
                           gamecanvas18.screen.drawLine(i7, gamecanvas18.drawY + 12, i9, gamecanvas18.drawY + 12);
                           if (b4 > 0) {
                              spriteInstances[0].sprite.paintModule(gamecanvas18.screen, gamecanvas18.paintSpriteFrame, i9, gamecanvas18.drawY + 12 - 2, 0);
                           }
                           break label773;
                        case 34:
                           if (gamecanvas1.togglePhase != 9 && gamecanvas1.togglePhase >= 5) {
                              gamecanvas1.paintOverlaySet = 14;
                              gamecanvas1.paintOverlayFrame = 0 + gamecanvas1.togglePhase * 5 / 10;
                           }
                           break label773;
                        case 35:
                           if (gamecanvas1.togglePhase != 0 && gamecanvas1.togglePhase <= 5) {
                              gamecanvas1.paintOverlaySet = 15;
                              gamecanvas1.paintOverlayFrame = 4 - gamecanvas1.togglePhase * 5 / 10;
                              if (gamecanvas1.paintOverlayFrame < 0) {
                                 gamecanvas1.paintOverlayFrame = 0;
                              }
                           }
                           break label773;
                        case 36:
                           GameCanvas gamecanvas17 = gamecanvas1;
                           i7 = objectState[gamecanvas17.paintTileX][gamecanvas17.paintTileY] == 1 ? 1 : 0;
                           Sprite sprite3;
                           (sprite3 = sprites[5])
                              .paintAnimFrame(
                                 gamecanvas17.screen, i7, (stateTicks >> 1) % sprite3.getAnimFrameCount(i7), gamecanvas17.drawX, gamecanvas17.drawY, 0, 0, 0
                              );
                           break label773;
                        case 37:
                           gamecanvas1.paintOverlaySet = 17;
                           gamecanvas1.paintOverlayFrame = 0 + (i7 - 1) * 3 / 8;
                           break label773;
                        case 43:
                           gamecanvas1.selectRollingObjectSprite((byte)gamecanvas1.paintObject);
                           break label773;
                        case 44:
                           label695: {
                              GameCanvas gamecanvas16 = gamecanvas1;
                              gamecanvas1.paintSpriteAnim = (objectState[gamecanvas16.paintTileX][gamecanvas16.paintTileY] & 56) >> 3;
                              paintSprite = sprites[27];
                              GameCanvas gamecanvas37;
                              switch (gamecanvas16.paintSpriteAnim) {
                                 case 1:
                                    gamecanvas37 = gamecanvas16;
                                    i18 = (objectTimers[gamecanvas16.paintTileX][gamecanvas16.paintTileY] >> 1) % sprites[27].getAnimFrameCount(1);
                                    break;
                                 case 3:
                                    gamecanvas16.paintSpriteFrame = 0;
                                    gamecanvas16.paintOffsetY = -objectTimers[gamecanvas16.paintTileX][gamecanvas16.paintTileY];
                                    break label695;
                                 default:
                                    gamecanvas37 = gamecanvas16;
                                    i18 = objectTimers[gamecanvas16.paintTileX][gamecanvas16.paintTileY];
                              }

                              gamecanvas37.paintSpriteFrame = i18;
                           }

                           gamecanvas1.paintSpriteFrame = 0;
                           break label773;
                        case 45:
                           GameCanvas gamecanvas15 = gamecanvas1;
                           i7 = objectState[gamecanvas15.paintTileX][gamecanvas15.paintTileY];
                           paintSprite = sprites[28];
                           gamecanvas15.paintSpriteAnim = i7 & 15;
                           int l8 = (objectState[gamecanvas15.paintTileX][gamecanvas15.paintTileY] & 2088960) >> 13;
                           if (gamecanvas15.paintSpriteAnim != 10) {
                              int j11 = frameAtTime(paintSprite, gamecanvas15.paintSpriteAnim, l8);
                              gamecanvas15.paintSpriteFrame = j11;
                              int l12 = (paintSprite.animFrameStarts[gamecanvas15.paintSpriteAnim] + j11) * 5;
                              gamecanvas15.paintOffsetX = paintSprite.animFrames[l12 + 2];
                              gamecanvas15.paintOffsetY = paintSprite.animFrames[l12 + 3];
                           } else {
                              int i11 = l8;
                              int k12 = 0;
                              int k15 = paintSprite.getAnimFrameCount(gamecanvas15.paintSpriteAnim);
                              int l16 = 0;

                              while (true) {
                                 int i14 = l16;
                                 if (i11 <= 0) {
                                    gamecanvas15.paintSpriteFrame = k12;
                                    break;
                                 }

                                 i11 -= paintSprite.getAnimFrameDuration(gamecanvas15.paintSpriteAnim, i14);
                                 k12 = i14;
                                 l16 = (i14 + 1) % k15;
                              }
                           }

                           gamecanvas15.adjustForWindPod();
                           break label773;
                        case 46:
                           GameCanvas gamecanvas14 = gamecanvas1;
                           paintSprite = sprites[29];
                           gamecanvas14.paintSpriteAnim = objectState[gamecanvas14.paintTileX][gamecanvas14.paintTileY] & 31;
                           GameCanvas gamecanvas36;
                           if (gamecanvas14.paintSpriteAnim != 8 && gamecanvas14.paintSpriteAnim != 9) {
                              i7 = (objectState[gamecanvas14.paintTileX][gamecanvas14.paintTileY] & 8160) >> 5;
                              int k8 = frameAtTime(paintSprite, gamecanvas14.paintSpriteAnim, i7);
                              gamecanvas14.paintSpriteFrame = k8;
                              int l10 = (paintSprite.animFrameStarts[gamecanvas14.paintSpriteAnim] + k8) * 5;
                              gamecanvas14.paintOffsetX = paintSprite.animFrames[l10 + 2];
                              gamecanvas36 = gamecanvas14;
                              i18 = paintSprite.animFrames[l10 + 3];
                           } else {
                              gamecanvas14.paintSpriteFrame = 0;
                              i7 = paintSprite.animFrameStarts[gamecanvas14.paintSpriteAnim] * 5;
                              gamecanvas14.paintOffsetX = paintSprite.animFrames[i7 + 2];
                              gamecanvas36 = gamecanvas14;
                              i18 = -objectTimers[gamecanvas14.paintTileX][gamecanvas14.paintTileY];
                           }

                           gamecanvas36.paintOffsetY = i18;
                           break label773;
                        case 47:
                           gamecanvas35 = gamecanvas1;
                           break;
                        case 48:
                           GameCanvas gamecanvas13 = gamecanvas1;
                           if (((i7 = objectState[gamecanvas13.paintTileX][gamecanvas13.paintTileY]) & 8) == 0) {
                              paintSprite = sprites[32];
                              int j8 = i7 & 7;
                              boolean flag;
                              GameCanvas gamecanvas34;
                              if ((!(flag = (i7 & 16) == 0) || j8 != 2) && (flag || j8 != 4)) {
                                 gamecanvas34 = gamecanvas13;
                                 i18 = flag ? 1 : 0;
                              } else {
                                 gamecanvas34 = gamecanvas13;
                                 i18 = 2;
                              }

                              gamecanvas34.paintSpriteFrame = i18;
                              byte b8 = objectTimers[gamecanvas13.paintTileX][gamecanvas13.paintTileY];
                              gamecanvas13.paintOffsetX = b8 * directionOffsets[j8];
                              gamecanvas13.paintOffsetY = b8 * directionOffsets[j8 + 8];
                           }
                           break label773;
                        case 49:
                           GameCanvas gamecanvas12 = gamecanvas1;
                           i7 = gamecanvas1.paintTileX;
                           int i8 = gamecanvas12.paintTileY;
                           int k10 = objectState[i7][i8];
                           byte b7 = objectTimers[i7][i8];
                           int k2 = k10 & 7;
                           gamecanvas12.paintOffsetX = b7 * directionOffsets[k2];
                           gamecanvas12.paintOffsetY = b7 * directionOffsets[k2 + 8];
                           paintSprite = sprites[38];
                           int l2;
                           GameCanvas gamecanvas32;
                           if (((l2 = (k10 & 28672) >> 12) != 2 && l2 != 4 && k2 != 2 && k2 != 4 || objectMap[i7 - 1][i8] < 0 || objectMap[i7 + 1][i8] < 0)
                              && (l2 != 1 && l2 != 3 && k2 != 1 && k2 != 3 || objectMap[i7][i8 - 1] < 0 || objectMap[i7][i8 + 1] < 0)) {
                              GameCanvas gamecanvas33;
                              if (k2 != 1 && k2 != 3) {
                                 gamecanvas33 = gamecanvas12;
                                 i18 = 0;
                              } else {
                                 gamecanvas33 = gamecanvas12;
                                 i18 = k2 - 1;
                              }

                              gamecanvas33.paintSpriteAnim = i18;
                              gamecanvas32 = gamecanvas12;
                              i18 = (stateTicks >> 1) % paintSprite.getAnimFrameCount(gamecanvas12.paintSpriteAnim);
                           } else {
                              gamecanvas12.paintSpriteAnim = 1;
                              gamecanvas32 = gamecanvas12;
                              i18 = 0;
                           }

                           gamecanvas32.paintSpriteFrame = i18;
                           gamecanvas12.adjustForWindPod();
                           break label773;
                     }

                     gamecanvas35.computeObjectOffset();
                  }

                  if (gamecanvas1.paintOverlaySet != -1) {
                     if (imageSets[gamecanvas1.paintOverlaySet] == null) {
                        sprites[themedSpriteIndex(gamecanvas1.paintOverlaySet)]
                           .paintFrame(
                              graphics2,
                              gamecanvas1.paintOverlayFrame,
                              gamecanvas1.drawX + gamecanvas1.paintOffsetX,
                              gamecanvas1.drawY + gamecanvas1.paintOffsetY,
                              gamecanvas1.paintTransform,
                              0,
                              0
                           );
                     } else {
                        graphics2.drawImage(
                           imageSets[gamecanvas1.paintOverlaySet][gamecanvas1.paintOverlayFrame],
                           gamecanvas1.drawX + gamecanvas1.paintOffsetX,
                           gamecanvas1.drawY + gamecanvas1.paintOffsetY,
                           gamecanvas1.paintTransform
                        );
                     }

                     gamecanvas1.paintOverlaySet = -1;
                     gamecanvas1.paintTransform = 0;
                  } else {
                     if (paintSprite == null) {
                        continue;
                     }

                     if (gamecanvas1.paintTileX == gamecanvas1.deathX && gamecanvas1.paintTileY == gamecanvas1.deathY) {
                        deferredSprite = paintSprite;
                        gamecanvas1.deferredAnim = gamecanvas1.paintSpriteAnim;
                        gamecanvas1.deferredFrame = gamecanvas1.paintSpriteFrame;
                        gamecanvas1.deferredX = gamecanvas1.drawX + gamecanvas1.paintOffsetX;
                        gamecanvas1.deferredY = gamecanvas1.drawY + gamecanvas1.paintOffsetY;
                     }

                     if (gamecanvas1.paintSpriteAnim != -1) {
                        paintSprite.paintAnimFrame(
                           graphics2,
                           gamecanvas1.paintSpriteAnim,
                           gamecanvas1.paintSpriteFrame,
                           gamecanvas1.drawX + gamecanvas1.paintOffsetX,
                           gamecanvas1.drawY + gamecanvas1.paintOffsetY,
                           0,
                           0,
                           0
                        );
                        gamecanvas1.paintSpriteAnim = -1;
                     } else {
                        paintSprite.paintFrame(
                           graphics2,
                           gamecanvas1.paintSpriteFrame,
                           gamecanvas1.drawX + gamecanvas1.paintOffsetX,
                           gamecanvas1.drawY + gamecanvas1.paintOffsetY,
                           0,
                           0,
                           0
                        );
                     }

                     paintSprite = null;
                     gamecanvas1.unusedPaintValue = 0;
                  }

                  gamecanvas1.paintOffsetX = gamecanvas1.paintOffsetY = 0;
               }
            }
         }
      }

      if (pickupAnimStarting) {
         graphics.setColor(0);
         graphics.fillRect(0, 0, 320, 240);
         this.paintPlayer();
         if (this.drawnEffect != this.nextEffect) {
            int k17 = this.drawnEffect;

            while (true) {
               j = k17;
               if (k17 == this.nextEffect) {
                  break;
               }

               k = effectPositions[j << 1] * 24 - this.cameraX;
               l = effectPositions[(j << 1) + 1] * 24 - this.cameraY;
               Sprite sprite9;
               Graphics graphics9;
               byte b17;
               if (effectTypes[j] < 0) {
                  Sprite sprite2;
                  sprite9 = sprite2 = sprites[2];
                  graphics9 = graphics;
                  b17 = 0;
                  k18 = frameAtTime(sprite2, 0, effectTimers[j]);
               } else {
                  sprite9 = sprites[9];
                  graphics9 = graphics;
                  b17 = effectTypes[j];
                  k18 = effectTimers[j];
               }

               sprite9.paintAnimFrame(graphics9, b17, k18, k, l, 0, 0, 0);
               k17 = ++j & 7;
            }
         }

         this.screen.setClip(0, 0, 320, 240);
         graphics.translate(0, -31);
         this.paintHud();
      } else {
         this.paintPlayer();
         if (this.hasEnemies) {
            this.paintTileSlots(this.cameraTileOffsetX, this.cameraTileOffsetY);
         }

         if (this.collapsingRowCount > 0) {
            this.paintCollapsingRows();
         }

         if (this.stageKind != 0) {
            GameCanvas gamecanvas2 = this;
            switch (this.stageKind) {
               case 1:
                  gamecanvas2.paintStageAnimations();
                  break;
               case 2:
                  GameCanvas gamecanvas4 = gamecanvas2;
                  if (gamecanvas2.prologueMarkerHint && gamecanvas4.activeCutscene == null && gamecanvas4.pendingHint == -1) {
                     sprites[15].paintAnimFrame(gamecanvas4.screen, 0, stateTicks >> 1 & 3, gamecanvas4.playerScreenX, gamecanvas4.playerScreenY - 24, 0, 0, 0);
                  }

                  GameCanvas gamecanvas5 = gamecanvas2;
                  if (gamecanvas2.cameraX + 320 > 1440 && gamecanvas5.cameraY + 240 > 48) {
                     if (sprites[10] == null) {
                        sprites[10] = loadSprite("/mmv.f", 0);
                     }

                     Sprite sprite = sprites[10];
                     if (gamecanvas5.isPlayerAt(60, 3) || gamecanvas5.isPlayerAt(61, 3)) {
                        sprite.paintFrame(gamecanvas5.screen, 1, 1440 - gamecanvas5.cameraX, 48 - gamecanvas5.cameraY, 0, 0, 0);
                     }
                  }

                  if (gamecanvas5.playerX > 55 && gamecanvas5.playerVisible) {
                     spriteInstances[0].paint(gamecanvas5.screen);
                  }
                  break;
               case 3:
                  gamecanvas2.paintBossArena();
                  break;
               case 4:
                  gamecanvas2.paintBossParts();
                  break;
               case 5:
                  gamecanvas2.paintBoss();
            }
         }

         for (int i3 = -1; i3 < 9; i3++) {
            for (int j3 = -1; j3 < 15; j3++) {
               int i5 = j3 + this.cameraTileX;
               int j7 = i3 + this.cameraTileY;
               if (i5 >= 0 && i5 < this.mapWidth && j7 >= 0 && j7 < this.mapHeight) {
                  l = tileMap[i5][j7] & 0xFF;
                  byte b1 = objectMap[i5][j7];
                  if (l < 38 || l >= 80) {
                     int j10;
                     int j12;
                     label929: {
                        j10 = j3 * 24 - this.cameraTileOffsetX;
                        j12 = i3 * 24 - this.cameraTileOffsetY;
                        Sprite sprite7;
                        Graphics graphics6;
                        if (l >= 20 && l < 26) {
                           l -= 20;
                           sprite7 = sprites[3];
                           graphics6 = graphics;
                           j18 = l;
                           k18 = (i >> 2) % (sprites[3].animFrameCounts[l] & 255);
                        } else {
                           switch (l) {
                              case 31:
                                 l = tileMap[i5][j7] >> 8;
                                 Sprite sprite5 = sprites[15];
                                 int j15 = (stateTicks >> 1) % (sprite5.animFrameCounts[l] & 255);
                                 sprite5.paintAnimFrame(this.screen, l, j15, j10, j12, 0, 0, 0);
                                 break;
                              case 32:
                                 sprites[16].paintAnimFrame(this.screen, 0, tileMap[i5][j7] >> 8 & 0xFF, j10, j12, 0, 0, 0);
                                 break;
                              case 36:
                                 l = (tileMap[i5][j7] >> 8) - 1;
                                 l = (0 + l * 7) / 16;
                                 sprites[20].paintAnimFrame(graphics, 0, l, j10, j12, 0, 0, 0);
                                 break;
                              default:
                                 byte b0;
                                 if ((b0 = (byte)(l - 80)) >= 0) {
                                    sprites[42].paintFrame(graphics, b0, j10, j12, 0, 0, 0);
                                 }
                           }

                           if ((l = (tileMap[i5][j7] & -268435456) >> 28) <= 0) {
                              break label929;
                           }

                           sprite7 = sprites[13];
                           graphics6 = graphics;
                           j18 = 0;
                           k18 = l;
                        }

                        sprite7.paintAnimFrame(graphics6, j18, k18, j10, j12, 0, 0, 0);
                     }

                     if (b1 == 54) {
                        sprites[7].paintAnimFrame(graphics, 0, frameAtTime(sprites[7], 0, objectState[i5][j7]), j10, j12, 0, 0, 0);
                     }
                  }
               }
            }
         }

         if (this.overlayPending) {
            j = (this.overlayTileX - this.cameraTileX) * 24 - this.cameraTileOffsetX + this.overlayDx;
            k = (this.overlayTileY - this.cameraTileY + 1) * 24 - this.cameraTileOffsetY + this.overlayDy;
            graphics.drawImage(sprites[13].moduleImages[0][0 + this.overlayModule], j, k, 0);
            this.overlayPending = false;
         }

         if (this.hintIconSet != -1) {
            graphics.drawImage(imageSets[13][0], this.playerScreenX + -12, this.playerScreenY + -24 + 2, 3);
            graphics.drawImage(imageSets[this.hintIconSet][this.hintIconFrame], this.playerScreenX + -12, this.playerScreenY + -24, 3);
         }

         if (this.drawnEffect != this.nextEffect) {
            int j17 = this.drawnEffect;

            while (true) {
               j = j17;
               if (j17 == this.nextEffect) {
                  break;
               }

               k = effectPositions[j << 1] * 24 - this.cameraX;
               l = effectPositions[(j << 1) + 1] * 24 - this.cameraY;
               Sprite sprite8;
               Graphics graphics7;
               byte b15;
               if (effectTypes[j] < 0) {
                  Sprite sprite1;
                  sprite8 = sprite1 = sprites[2];
                  graphics7 = graphics;
                  b15 = 0;
                  k18 = frameAtTime(sprite1, 0, effectTimers[j]);
               } else {
                  sprite8 = sprites[9];
                  graphics7 = graphics;
                  b15 = effectTypes[j];
                  k18 = effectTimers[j];
               }

               sprite8.paintAnimFrame(graphics7, b15, k18, k, l, 0, 0, 0);
               j17 = ++j & 7;
            }
         }

         this.cameraY = this.cameraY + this.screenShakeOffset;
         if (this.cheatSkipWalking) {
            drawMessageBox(graphics, fontMedium, strings[15], 160, 120, 17, 19, false);
         }

         if (this.stageTitleUntil > stateTicks && this.stageKind != 2) {
            if (!this.bossExitPending && !this.crystalExitPending) {
               k = (j = this.stageTitleUntil - stateTicks) < 20 ? (j - 10) * 320 / 20 : (j >= 50 ? (60 - j) * 320 / 15 : 160);
               l = 320 - k;
               drawMessageBox(graphics, fontMedium, strings[worldNameStrings[this.world]], k, 15, 17, 20, false);
               drawMessageBox(graphics, fontMedium, stageNames[stageNumbers[this.world][this.stage] - 1], l, 50, 17, 20, false);
            }
         } else if (this.checkpointBannerUntil > stateTicks) {
            drawMessageBox(graphics, fontMedium, strings[13], 160, Constants.checkpointTextY, 17, 20, false);
         } else if (this.checkpointBannerUntil == stateTicks && this.checkpointBannerCount <= 2) {
            this.checkpointBannerCount++;
            this.storeLives();
         }

         label581: {
            GameCanvas gamecanvas46;
            Graphics graphics8;
            byte b16;
            switch (this.stageKind) {
               case 3:
                  if (this.bossAnim == -1 || this.bossAnim == 15) {
                     break label581;
                  }

                  gamecanvas46 = this;
                  graphics8 = graphics;
                  b16 = 5;
                  break;
               case 4:
                  GameCanvas gamecanvas3 = this;
                  Graphics graphics3 = this.screen;
                  SpriteInstance spriteinstance = spriteInstances[5];
                  if (gamecanvas3.bossAnim == 7) {
                     i1 = spriteinstance.x + stateTicks * gamecanvas3.bossTimer % 48;
                     int j5 = spriteinstance.y;
                     if (gamecanvas3.bossPreviousAnim == 10) {
                        j5 -= 144;
                     }

                     sprites[7].paintAnimFrame(graphics3, 1, stateTicks % sprites[7].getAnimFrameCount(1), i1, j5, 0, 0, 0);
                     gamecanvas3.hurtPlayerNear(i1, j5);
                  } else if (gamecanvas3.bossAnim != 8 && gamecanvas3.bossAnim != 0) {
                     gamecanvas3.drawBossHealth(graphics3, 3);
                  }
                  break label581;
               case 5:
                  if (this.bossAnim != -1 && this.bossAnim != 15 && this.bossAnim != 13 && this.bossAnim != 12) {
                     gamecanvas46 = this;
                     graphics8 = graphics;
                     b16 = 4;
                     break;
                  }
               default:
                  break label581;
            }

            gamecanvas46.drawBossHealth(graphics8, b16);
         }

         graphics.translate(0, -31);
         graphics.setClip(0, 0, 320, 240);
         if (this.activeCutscene == null) {
            this.paintHud();
         }

         if (this.hintText != null) {
            this.hudDirty = true;
            drawMessageBox(graphics, fontSmall, wrapForScreen(this.hintText), 160, 152, 17, 4, true);
         }

         if (this.activeCutscene != null && !pickupAnimStarting) {
            this.hudDirty = true;
            this.activeCutscene.paint(graphics);
         }

         if (this.fadeHeight > 0) {
            this.hudDirty = true;
            graphics.setColor(0);
            graphics.fillRect(0, 0, 320, this.fadeHeight);
            graphics.fillRect(0, 240 - this.fadeHeight, 320, this.fadeHeight);
            graphics.translate(0, 31);
            this.paintPlayer();
            if (deferredSprite != null) {
               if (this.deferredAnim != -1) {
                  deferredSprite.paintAnimFrame(
                     graphics,
                     this.deferredAnim,
                     this.deferredFrame,
                     (this.deathX - this.cameraTileX) * 24 - this.cameraTileOffsetX,
                     (this.deathY - this.cameraTileY) * 24 - this.cameraTileOffsetY,
                     0,
                     0,
                     0
                  );
               } else {
                  deferredSprite.paintFrame(graphics, this.deferredFrame, this.deferredX, this.deferredY, 0, 0, 0);
               }
            }

            graphics.translate(0, -31);
         }
      }
   }

   private void paintCollapsingRows() {
      int i = this.cameraY - 24;
      int j = this.cameraY + 240;
      int k = this.cameraX;
      int l = this.cameraX + 320;
      int i1 = (stateTicks >> 1) % sprites[2].getAnimFrameCount(1);

      for (int b0 = 0; b0 < collapsingRows.length; b0 += 3) {
         int j1 = 0;
         byte b1;
         int k1;
         if ((k1 = (b1 = collapsingRows[b0 + 2]) * 24) > i && k1 < j) {
            int l1 = collapsingRows[b0];
            byte b2 = collapsingRows[b0 + 1];
            int i2 = l1 * 24;
            int j2 = b2 * 24 + 24;
            if (i2 >= k && i2 <= l || j2 >= k && j2 <= l || i2 <= k && j2 >= l) {
               i2 -= k;
               l1 += l1 != 0 ? -1 : 0;
               boolean flag = true;
               if (objectMap[l1][b1] == 48 && (objectState[l1][b1] & 8) != 0) {
                  i2 -= 12;
                  flag = false;
                  if ((objectState[l1][b1 + 1] & 7) == 3) {
                     j1 = -objectTimers[l1][b1 + 1];
                  } else {
                     i2 -= -objectTimers[l1][b1 + 1];
                  }
               }

               j2 -= k;
               l1 = b2 + (b2 < this.mapWidth - 1 ? 1 : 0);
               boolean flag1 = true;
               if (objectMap[l1][b1] == 48 && (objectState[l1][b1] & 8) != 0) {
                  j2 += 12;
                  flag1 = false;
                  if ((objectState[l1][b1 + 1] & 7) == 3) {
                     j1 = -objectTimers[l1][b1 + 1];
                  } else {
                     j2 -= objectTimers[l1][b1 + 1];
                  }
               }

               j1 = k1 - this.cameraY + 10 + j1;
               this.screen.setColor(1820159);
               this.screen.drawLine(i2, j1, j2, j1);
               j1 += 2;
               this.screen.drawLine(i2, j1, j2, j1);
               j1--;
               this.screen.setColor(14153215);
               this.screen.drawLine(i2, j1, j2, j1);
               Sprite sprite;
               Graphics graphics;
               byte b3;
               int k2;
               int l2;
               int i3;
               byte b4;
               if (flag) {
                  sprite = sprites[2];
                  graphics = this.screen;
                  b3 = 1;
                  k2 = i1;
                  l2 = i2;
                  i3 = j1;
                  b4 = 1;
               } else {
                  if (!flag1) {
                     continue;
                  }

                  sprite = sprites[2];
                  graphics = this.screen;
                  b3 = 1;
                  k2 = i1;
                  l2 = j2;
                  i3 = j1;
                  b4 = 0;
               }

               sprite.paintAnimFrame(graphics, b3, k2, l2, i3, b4, 0, 0);
            }
         }
      }
   }

   private void adjustForWindPod() {
      if ((objectState[this.paintTileX][this.paintTileY] & 7) == 1 && (tileMap[this.paintTileX][this.paintTileY] & 0xFF) == 35) {
         this.paintOffsetX = 0;
         this.paintOffsetY = objectTimers[this.paintTileX][this.paintTileY];
      }
   }

   private void paintTileSlots(int var1, int var2) {
      Graphics graphics = this.screen;

      for (int i = 0; i < 9; i++) {
         for (int j = 0; j < 15; j++) {
            int l = j + this.cameraX / 24;
            int i1 = i + this.cameraY / 24;
            if (l >= 0 && l < this.mapWidth && i1 >= 0 && i1 < this.mapHeight) {
               int k = tileSlots[l][i1];
               int j1 = j * 24 - var1;
               int k1 = i * 24 - var2;
               if (k > 0) {
                  int i3 = 0;

                  while (true) {
                     byte b1 = (byte)i3;
                     if (i3 > 2) {
                        break;
                     }

                     if (slotBits(k, b1, (byte)0, (byte)3) != 0) {
                        byte b0;
                        if ((b0 = (byte)(slotBits(k, b1, (byte)7, (byte)2) << 3)) > 0) {
                           b0 = actorBits(slotBits(k, b1, (byte)0, (byte)3), (byte)45, (byte)2) <= 1 ? (byte)(b0 - 24) : (byte)(24 - b0);
                           this.clipToPlayfield();
                           graphics.clipRect(j1, k1 + (b1 << 3), 24, 8);
                        }

                        byte b2 = b1;
                        int j2 = k;
                        int i2 = i1;
                        int l1 = l;
                        byte b3;
                        int k2 = actorBits(b3 = (byte)slotBits(j2, b2, (byte)0, (byte)3), (byte)31, (byte)6);
                        int l2 = actorBits(b3, (byte)37, (byte)6);
                        b3 = (byte)actorBits(b3, (byte)43, (byte)2);   // 2-bit field
                        j2 = slotBits(j2, b2, (byte)3, (byte)4);
                        if (k2 == l1 && l2 == i2 && b3 == b2) {
                           label91: {
                              byte b4;
                              switch (j2) {
                                 case 4:
                                    b4 = 1;
                                    break;
                                 case 5:
                                    b4 = 2;
                                    break;
                                 default:
                                    break label91;
                              }

                              j2 = b4;
                           }

                           i3 = j2 << 1;
                        } else {
                           switch (j2) {
                              case 6:
                                 clearTileSlot(l1, i2, b2);
                              default:
                                 i3 = j2 << 1;
                           }
                        }

                        l1 = i3;
                        j2 = ((i2 = i3 >> 1) == 7 ? stateTicks >> 3 : stateTicks) & 1;
                        if (i2 == 15) {
                           slotSprite.paintFrame(graphics, l1 + this.slotFrameCycle, j1 + b0 - 8, k1 + (b1 << 3) + 8, 36, 0, 0);
                           this.slotFrameCycle++;
                           if (this.slotFrameCycle > 2) {
                              this.slotFrameCycle = 0;
                           }
                        } else {
                           label123: {
                              Sprite sprite;
                              Graphics graphics1;
                              int j3;
                              int k3;
                              int l3;
                              if (i2 != 14 && i2 != 11) {
                                 if (i2 == 8 && b1 == 0 && tileSlots[l][i1 - 1] > 0) {
                                    slotSprite.paintFrame(graphics, 33, j1 + b0, k1, 20, 0, 0);
                                    b1 = 3;
                                    break label123;
                                 }

                                 sprite = slotSprite;
                                 graphics1 = graphics;
                                 j3 = l1 + j2;
                                 k3 = j1 + b0;
                                 l3 = k1 + (b1 << 3);
                              } else {
                                 sprite = slotSprite;
                                 graphics1 = graphics;
                                 j3 = l1 + j2;
                                 k3 = j1 + b0;
                                 l3 = k1;
                              }

                              sprite.paintFrame(graphics1, j3, k3, l3, 20, 0, 0);
                              this.clipToPlayfield();
                           }
                        }
                     }

                     i3 = (byte)(b1 + 1);
                  }
               }
            }
         }
      }
   }

   private void paintHud() {
      boolean flag = false;
      Graphics graphics = this.screen;
      Sprite sprite;
      if ((sprite = sprites[0]) != null) {
         graphics.translate(160, 240);
         Sprite sprite1;
         if (!this.hudDirty && gameState != 2) {
            flag = true;
            graphics.setClip(-160, -240, 320, 240);
            sprite1 = sprite;
         } else {
            sprite.paintFrame(graphics, 0, 0, 0, 0, 0, 0);
            if (this.hudPanelVisible) {
               sprite.paintFrame(graphics, 19, -159, 0, 0, 0, 0);
            }

            sprite1 = sprite;
         }

         sprite1.paintFrame(graphics, 1, 0, 0, 0, 0, 0);
         if (this.hasCompass) {
            sprite.paintFrame(graphics, 2, -150, 0, 0, 0, 0);
            sprite.paintFrame(graphics, 3 + this.compassDirection, -150, 0, 0, 0, 0);
         }

         if (flag) {
            graphics.setClip(-160, -240, 320, 240);
         }

         if (this.energy != this.hudEnergy || this.hudDirty || this.energy <= 1) {
            Image[] aimage = sprites[0].moduleImages[0];
            int j = this.energy <= 1 ? 1 : 0;
            int k = -33 - (saveData[8] - 4) * aimage[j + 11].getWidth() / 2;
            graphics.drawImage(aimage[j + 11], k, -25, 0);
            int i = 0 + aimage[j + 11].getWidth();
            int l = aimage[15].getWidth();

            for (int i1 = 0; i1 < saveData[8]; i1++) {
               Graphics graphics1;
               Image[] aimage1;
               byte b0;
               if ((this.energy > 1 || i1 != 0 || (stateTicks >> 2 & 1) != 0) && (i1 >= this.energy || this.energy <= 1)) {
                  graphics1 = graphics;
                  aimage1 = aimage;
                  b0 = 13;
               } else {
                  graphics1 = graphics;
                  aimage1 = aimage;
                  b0 = 15;
               }

               graphics1.drawImage(aimage1[b0 + j], k + i, -25, 0);
               i += l;
            }

            graphics.drawImage(aimage[j + 17], k + i, -25, 0);
            this.hudEnergy = this.energy;
         }

         if (this.hudDiamonds != this.stageDiamonds || this.hudPadlockGems != this.padlockGemsRemaining || this.hudDirty) {
            drawNumber(graphics, 110, -12, this.stageDiamonds, sprites[0].moduleImages[0], 0);
         }

         if (this.hudRedDiamonds != this.stageRedDiamonds || this.hudDirty) {
            this.hudRedDiamonds = this.stageRedDiamonds;
            drawNumber(graphics, 147, -12, this.stageRedDiamonds, sprites[0].moduleImages[0], 0);
         }

         graphics.translate(-160, -240);
         graphics.translate(160, 0);
         flag = false;
         if (this.hudDirty || gameState == 2) {
            sprite.paintFrame(graphics, 20, 0, 0, 0, 0, 0);
            flag = true;
         }

         if (this.hudGoldKeys != this.goldKeys || this.hudSilverKeys != this.silverKeys || this.hudDirty || gameState == 2 || flag) {
            if (!flag) {
               sprite.paintFrame(graphics, 20, 0, 0, 0, 0, 0);
               flag = true;
            }

            drawNumber(graphics, 47, 12, this.goldKeys, sprites[0].moduleImages[0], 0);
            drawNumber(graphics, 87, 12, this.silverKeys, sprites[0].moduleImages[0], 0);
            this.hudGoldKeys = this.goldKeys;
            this.hudSilverKeys = this.silverKeys;
         }

         if (this.hudLives != this.lives || this.hudDirty || flag) {
            if (!flag) {
               sprite.paintFrame(graphics, 20, 0, 0, 0, 0, 0);
            }

            drawNumber(graphics, -29, 12, this.lives, sprites[0].moduleImages[0], 0);
            this.hudLives = this.lives;
         }

         graphics.translate(-160, 0);
         this.hudDiamonds = this.stageDiamonds;
         this.hudPadlockGems = this.padlockGemsRemaining;
         this.hudDirty = false;
      }
   }

   private void paintSideObject(byte var1) {
      SpriteInstance spriteinstance = spriteInstances[4];
      if (var1 == 23) {
         spriteinstance.x = this.drawX;
         spriteinstance.flags |= 1;
      } else {
         spriteinstance.flags &= -2;
         spriteinstance.x = this.drawX + 24;
      }

      spriteinstance.y = this.drawY;
      spriteinstance.applyFrameStep();
      spriteinstance.paint(this.screen);
   }

   private void drawSeparator(int var1) {
      for (int i = var1 - 8 - 1; i < var1 + 15 - 2; i++) {
         sprites[18].paintFrame(this.screen, 4, i % 2, i, 0, 0, 0);
      }

      this.screen.setColor(16777215);
      this.screen.drawLine(0, var1 - 8 - 1 - 1, 320, var1 - 8 - 1 - 1);
      this.screen.drawLine(0, var1 + 15 - 1, 320, var1 + 15 - 1);
      this.screen.setColor(0);
      this.screen.drawLine(0, var1 - 8 - 1 - 2, 320, var1 - 8 - 1 - 2);
      this.screen.drawLine(0, var1 + 15 - 2, 320, var1 + 15 - 2);
   }

   private void paintMenu() {
      if (this.menuGameStartedStale) {
         GameCanvas gamecanvas = this;
         this.menuGameStartedStale = false;
         gamecanvas.menuGameStarted = hasStartedGame();
      }

      int i = 240 - ((this.menuItemCount + 1) * Constants.menuRowHeight + 1 + 2) + (!this.menuGameStarted && this.menu == 0 ? Constants.menuRowHeight : 0);
      int i1 = 240 - Constants.menuRowHeight + 1 + 1;
      this.screen.setClip(0, 0, 320, 240);
      if (gameState == 2 && menuNeedsRedraw && pauseBackgroundStale) {
         this.hudPanelVisible = false;
         this.paintPlaying();
         this.hudPanelVisible = true;
         pauseBackgroundStale = false;
      }

      if (gameState == 7 || gameState == 2) {
         int j = i - (120 - (i1 - i) / 2);
         i -= j;
         i1 -= j;
      }

      if (this.menu == 7) {
         this.drawSeparator(i - 22);
         fontMedium.drawString(this.screen, strings[this.subState == 5 ? 23 : 2], 160, i - 20, 17);
      }

      if (gameState == 7) {
         Graphics graphics1;
         byte b2;
         int j4;
         short short2;
         int k4;
         if (this.menuHighlightedRow != -1 && !menuNeedsRedraw) {
            int j1 = i + this.menuHighlightedRow * Constants.menuRowHeight;
            this.screen.setColor(0);
            graphics1 = this.screen;
            b2 = 0;
            j4 = j1;
            short2 = 320;
            k4 = Constants.menuRowHeight + 1;
         } else {
            this.screen.setColor(0);
            graphics1 = this.screen;
            b2 = 0;
            j4 = 0;
            short2 = 320;
            k4 = 240;
         }

         graphics1.fillRect(b2, j4, short2, k4);
      }

      long k1 = System.currentTimeMillis();
      if (this.menuAnimFrame >= 0 && k1 - this.menuAnimTime > 100L) {
         this.menuAnimFrame++;
         this.menuAnimTime = k1;
      }

      if (this.menuHighlightedRow != -1 && !menuNeedsRedraw) {
         int l1 = this.menu == 0 && this.menuHighlightedRow > 0 && !this.menuGameStarted ? Constants.menuRowHeight : 0;
         int i2 = i + this.menuHighlightedRow * Constants.menuRowHeight - l1;
         this.screen.setClip(0, i2, 320, Constants.menuRowHeight + 1);
      }

      if (this.menuHighlightedRow != this.menuSelection || menuNeedsRedraw) {
         int k = i1;
         int j2 = i;
         GameCanvas gamecanvas1 = this;
         if (gameState == 4) {
            drawSplash(gamecanvas1.screen, false);

            for (int l = j2 - 1; l < k - 2; l++) {
               sprites[18].paintFrame(gamecanvas1.screen, 4, l % 2, l, 0, 0, 0);
            }
         }

         if (gameState == 2) {
            gamecanvas1.screen.setColor(0);
            gamecanvas1.screen.fillRect(0, j2 - 1, 320, k - 2 - (j2 - 1));
         }

         gamecanvas1.screen.setColor(16777215);
         gamecanvas1.screen.drawLine(0, j2 - 1 - 1, 320, j2 - 1 - 1);
         gamecanvas1.screen.drawLine(0, k - 1, 320, k - 1);
         gamecanvas1.screen.setColor(0);
         gamecanvas1.screen.drawLine(0, j2 - 1 - 2, 320, j2 - 1 - 2);
         gamecanvas1.screen.drawLine(0, k - 2, 320, k - 2);
      }

      if (!this.menuGameStarted && gameState == 4 && this.menuSelection == 0 && this.subState != 5 && this.menu != 8) {
         this.menuSelection = 1;
      }

      for (int k2 = 0; k2 < this.menuItemCount; k2++) {
         if ((this.menuHighlightedRow == -1 || k2 == this.menuHighlightedRow || k2 == this.menuSelection || menuNeedsRedraw)
            && (this.menu != 0 || k2 != 0 || this.menuGameStarted)) {
            i1 = i + k2 * Constants.menuRowHeight + Constants.menuRowHeight / 2;
            if (this.menu == 0 && k2 > 0 && !this.menuGameStarted) {
               i1 -= Constants.menuRowHeight;
            }

            int l2 = 16777215;
            if (k2 == 2 && this.menu == 0 && isMoreGamesEnabled()) {
               l2 = 16711680;
            }

            if (k2 == 0 && this.menu == 8 && isMoreGamesEnabled()) {
               l2 = 16711680;
            }

            label279: {
               Graphics graphics;
               int j3;
               int l3;
               short short1;
               int i4;
               label370: {
                  if (this.stageKind == 2 && k2 == 3 && this.menu == 1) {
                     l2 = 16777215;
                     if (k2 != this.menuSelection) {
                        this.screen.setColor(13421772);
                        graphics = this.screen;
                        j3 = 0;
                        l3 = i1 - Constants.menuRowHeight / 2 + 1;
                        short1 = 320;
                        i4 = Constants.menuRowHeight - 1;
                        break label370;
                     }

                     graphics = this.screen;
                     j3 = 6710886;
                  } else {
                     if (k2 != this.menuSelection) {
                        break label279;
                     }

                     graphics = this.screen;
                     j3 = 13540096;
                  }

                  graphics.setColor(j3);
                  graphics = this.screen;
                  j3 = 0;
                  l3 = i1 - Constants.menuRowHeight / 2;
                  short1 = 320;
                  i4 = Constants.menuRowHeight + 1;
               }

               graphics.fillRect(j3, l3, short1, i4);
            }

            fontMedium.measure(strings[menus[this.menu][(k2 << 1) + 1]]);
            int i3 = FontRenderer.measuredWidth;
            if ((k2 != 2 || this.menu != 0 || !isMoreGamesEnabled()) && k2 == 0 && this.menu == 8) {
               isMoreGamesEnabled();
            }

            FontRenderer.setColor(l2);
            fontMedium.drawStringColored(this.screen, strings[menus[this.menu][(k2 << 1) + 1]], 160 - i3 / 2, i1 + 1, l2, 6);
            if (k2 == this.menuSelection) {
               this.screen.setColor(16777215);
               sprites[18].paintFrame(this.screen, 2, 160 - i3 / 2 - 8, i1, 0, 0, 10);
               sprites[18].paintFrame(this.screen, 2, 160 + i3 / 2 + 8, i1, 0, 0, 6);
            }
         }
      }

      this.menuHighlightedRow = this.menuSelection;
      this.menuTicks++;
      GameCanvas gamecanvas8;
      int k3;
      if (this.menuRevealDelay < 0) {
         gamecanvas8 = this;
         k3 = 3;
      } else {
         gamecanvas8 = this;
         k3 = this.menuRevealDelay - 1;
      }

      gamecanvas8.menuRevealDelay = k3;
      if (this.menuRevealDelay == 0 && this.menuItemsRevealed + 1 < this.menuItemCount) {
         this.menuItemsRevealed++;
      }

      this.screen.setClip(0, 0, 320, 240);
      if (this.menu != 0 && this.menu != 3 && this.menu != 1 && this.menu != 7 && this.menu != 8) {
         this.drawSelectSoftKey();
      }

      this.drawBackSoftKey();
      menuNeedsRedraw = false;
      if (this.menuAnimFrame == 2) {
         if (gameState == 2) {
            menuNeedsRedraw = true;
            pauseBackgroundStale = true;
         }

         this.menuAnimFrame = -1;
         this.hudDirty = true;
         this.menuHighlightedRow = -1;
         GameCanvas gamecanvas2 = this;
         FontRenderer.setColor(16777215);
         gamecanvas2.menuItemChosen = true;
         switch (gamecanvas2.menu) {
            case 0:
               GameCanvas gamecanvas7 = gamecanvas2;
               switch (gamecanvas2.selectedMenuAction()) {
                  case 0:
                     gamecanvas7.sound.stop();
                     gamecanvas7.loadSave();
                     if (saveData != null && hasStartedGame()) {
                        gamecanvas7.tipIndex = 0;
                        gamecanvas7.reloadLivesOnLoad = true;
                        gamecanvas7.hasCompass = true;
                        gamecanvas7.loadingStep = 0;
                        gameState = 28;
                        return;
                     }
                  case 1:
                     gamecanvas7.sound.stop();
                     if (!hasStartedGame()) {
                        gamecanvas7.startNewGame();
                     } else {
                        gamecanvas7.blackScreenPending = true;
                        gamecanvas7.creatingNewGame = false;
                        gameState = 31;
                     }

                     return;
                  case 2:
                     gamecanvas7.openMenu(5);
                     gamecanvas7.sound.stop();
                     return;
                  case 3:
                     gameState = 33;
                     gamecanvas7.sound.stop();
                     gamecanvas7.fullRedraw = true;
                     return;
                  case 4:
                     gameState = 22;
                     gamecanvas7.subState = 0;
                     gamecanvas7.sound.stop();
                     return;
                  case 5:
                     gamecanvas7.openMenu(7);
                     gamecanvas7.subState = 5;
                     return;
                  case 6:
                     gamecanvas7.openMoreGames();
                     return;
                  default:
                     gamecanvas7.menuItemChosen = false;
                     return;
               }
            case 1:
               GameCanvas gamecanvas6 = gamecanvas2;
               switch (gamecanvas2.selectedMenuAction()) {
                  case 0:
                     gameState = 1;
                     mapPreview = null;
                     return;
                  case 1:
                     gamecanvas6.openMenu(7);
                     gamecanvas6.subState = 1;
                     return;
                  case 2:
                     gamecanvas6.openMenu(5);
                     return;
                  case 3:
                     if (gamecanvas6.stage == 13 && gamecanvas6.world == 0) {
                        return;
                     }

                     gamecanvas6.openMenu(7);
                     gamecanvas6.subState = 3;
                     return;
                  case 4:
                     gamecanvas6.openMenu(7);
                     gamecanvas6.subState = 4;
                     return;
                  case 5:
                     gamecanvas6.openMenu(7);
                     gamecanvas6.subState = 5;
                     return;
                  case 6:
                     gameState = 33;
                     gamecanvas6.fullRedraw = true;
                     return;
                  default:
                     gamecanvas6.menuItemChosen = false;
                     return;
               }
            case 2:
               GameCanvas gamecanvas5 = gamecanvas2;
               switch (gamecanvas2.selectedMenuAction()) {
                  case 0:
                     gamecanvas5.sound.stop();
                     gamecanvas5.world = 0;
                     gamecanvas5.hasEnemies = false;
                     gameState = 15;
                     unlockedStage = getWorldProgress(gamecanvas5.world);
                     gamecanvas5.unloadMapOnLoad = true;
                     gamecanvas5.openWorldMap();
                     return;
                  case 1:
                     gamecanvas5.sound.stop();
                     gamecanvas5.world = 1;
                     gamecanvas5.stage = 0;
                     gameState = 15;
                     unlockedStage = getWorldProgress(gamecanvas5.world);
                     gamecanvas5.unloadMapOnLoad = true;
                     gamecanvas5.openWorldMap();
                     gamecanvas5.hasCompass = true;
                     gamecanvas5.hasEnemies = false;
                     if (saveData[9] >= 1) {
                        return;
                     }

                     saveData[9] = 1;
                     return;
                  case 2:
                     gamecanvas5.sound.stop();
                     gamecanvas5.world = 2;
                     gamecanvas5.stage = 0;
                     gamecanvas5.hasEnemies = false;
                     gameState = 15;
                     unlockedStage = getWorldProgress(gamecanvas5.world);
                     gamecanvas5.unloadMapOnLoad = true;
                     gamecanvas5.openWorldMap();
                     gamecanvas5.hasCompass = true;
                     if (saveData[9] < 2) {
                        saveData[9] = 2;
                     }

                     if (saveData[8] >= 8) {
                        return;
                     }

                     saveData[8] = 8;
                     return;
                  case 3:
                     gamecanvas5.openMenu(4);
                     return;
                  default:
                     gameState = 3;
                     gamecanvas5.sound.shutdown();
                     midlet.exitGame();
                     return;
               }
            case 3:
               gamecanvas2.subState = 0;
               boolean flag = false;
               boolean flag1 = false;
               byte b1 = 5;
               int b0 = 0;

               while (true) {
                  if (b0 >= menus[5].length) {
                     return;
                  }

                  if (0 == menus[5][b0]) {
                     menus[5][b0 + 1] = 0;
                     break;
                  }

                  b0 += 2;
               }
            case 4:
            case 6:
            default:
               break;
            case 5:
               return;
            case 7:
               GameCanvas gamecanvas4 = gamecanvas2;
               switch (gamecanvas2.selectedMenuAction()) {
                  case 0:
                     if (gamecanvas4.previousMenu == 0) {
                        gameState = 4;
                     }

                     gamecanvas4.openMenu(gamecanvas4.previousMenu);
                     break;
                  case 1:
                     switch (gamecanvas4.subState) {
                        case 1:
                           gamecanvas4.startStage();
                        case 2:
                        default:
                           break;
                        case 3:
                           gameState = 15;
                           gamecanvas4.freeStageOnLoad = true;
                           gamecanvas4.unloadMapOnLoad = true;
                           gamecanvas4.openWorldMap();
                           break;
                        case 4:
                           gamecanvas4.freeStageResources();
                           gameState = 9;
                           gamecanvas4.loadingStepCount = 8;
                           gamecanvas4.openMenu(-1);
                           gamecanvas4.loadingStep = 0;
                           break;
                        case 5:
                           gameState = 3;
                     }
               }

               gamecanvas4.subState = -1;
               return;
            case 8:
               GameCanvas gamecanvas3 = gamecanvas2;
               switch (gamecanvas2.selectedMenuAction()) {
                  case 0:
                     gamecanvas3.openMoreGames();
                     break;
                  case 1:
                     gameState = 3;
               }

               gamecanvas3.subState = -1;
         }
      }
   }

   private void drawSelectSoftKey() {
      sprites[18].paintFrame(this.screen, 0, 303, 228, 0, 0, 0);
   }

   private void drawBackSoftKey() {
      sprites[18].paintFrame(this.screen, 3, 2, 228, 0, 0, 0);
   }

   private void paintLoading() {
      Graphics graphics = this.screen;
      this.screen.setColor(0);
      graphics.fillRect(0, 0, 320, 240);
      int i;
      if ((i = (this.loadingStep + 1) * 310 / this.loadingStepCount) > 310) {
         i = 310;
      }

      graphics.setColor(13540096);
      this.screen.fillRect(5, 230, i, 6);
      graphics.setColor(16554500);
      this.screen.drawRoundRect(4, 229, 311, 6, 2, 2);
      FontRenderer.setColor(16777215);
      fontMedium.drawString(this.screen, strings[35], 160, Constants.loadingTextY, 1);
   }

   private void paintBoss() {
      if (this.bossAnim != -1) {
         SpriteInstance spriteinstance;
         (spriteinstance = spriteInstances[5]).x = this.bossX - this.cameraX;
         spriteinstance.y = 504 - this.cameraY;
         spriteinstance.applyFrameStep();
         spriteinstance.paint(this.screen);
         if (this.bossAnim == 12) {
            int j = this.bossX - this.cameraX + stateTicks * this.bossTimer % 48;
            int i = spriteInstances[5].y + 24;
            sprites[7].paintAnimFrame(this.screen, 0, stateTicks % sprites[7].getAnimFrameCount(1), j, i, 0, 0, 0);
            this.hurtPlayerNear(j, i);
         }
      }
   }

   private void paintBossArena() {
      if (this.stageKind == 3 && stateTicks >= this.icicleDropTime + 80L) {
         for (int i = 14; i <= 21; i++) {
            objectState[i][15] = 0;
            tileMap[i][15] = -1;
            objectMap[i][15] = 44;
            objectTimers[i][15] = 0;
            tileRedrawTimers[i][15] = 24;
         }

         this.icicleDropTime = 0L;
      }

      if (this.bossAnim != 15) {
         if (this.cameraX + 320 + 48 >= this.yetiX && this.cameraY + 240 + 48 >= 504) {
            SpriteInstance spriteinstance;
            (spriteinstance = spriteInstances[5]).x = this.yetiX - this.cameraX;
            spriteinstance.y = 504 - this.cameraY;
            spriteinstance.applyFrameStep();
            spriteinstance.paint(this.screen);
         }

         if (this.bossAnim == 12) {
            int k = this.yetiX - this.cameraX + stateTicks * this.bossTimer % 48;
            int j = spriteInstances[5].y + 24;
            sprites[7].paintAnimFrame(this.screen, 0, stateTicks % sprites[7].getAnimFrameCount(0), k, j, 0, 0, 0);
            this.hurtPlayerNear(k, j);
         }
      }
   }

   private void drawBossHealth(Graphics var1, int var2) {
      var2 = var2 * 14 + 2;
      int i = 320 - var2 >> 1;
      if (this.bossHealth > 0) {
         var1.setColor(0);
         var1.fillRect(i, 5, var2, 12);
         var1.setColor(3913615);

         for (int j = 0; j < this.bossHealth; j++) {
            var1.fillRect(i + 2 + j * 14, 7, 12, 8);
         }
      }
   }

   private void paintBossParts() {
      Graphics graphics;
      SpriteInstance spriteinstance;
      SpriteInstance spriteinstance1;
      int i;
      label58: {
         graphics = this.screen;
         spriteinstance = spriteInstances[5];
         spriteinstance1 = spriteInstances[4];
         int l;
         switch (this.bossAnim) {
            case 1:
               l = this.bossTimer;
               break;
            case 2:
            case 7:
               l = 40;
               break;
            case 3:
               l = 40;
               break;
            case 4:
               l = 40 - (this.bossTimer << 1 << 1);
               break;
            case 5:
               i = 15 + this.bossTimer * 18;
               this.arenaWallsRestored = false;
               break label58;
            case 6:
            case 8:
            default:
               l = -1000;
               break;
            case 9:
               l = 15 + this.bossTimer * 18;
               break;
            case 10:
               l = 15 + this.bossTimer * 18;
               break;
            case 11:
               l = 15 + this.bossTimer * 18;
         }

         i = l;
      }

      label53: {
         spriteinstance.x = (10 + this.bossPosition * (2 + (this.bossPosition > 0 ? 1 : 0))) * 24 - this.cameraX;
         SpriteInstance spriteinstance2;
         int i1;
         if (this.bossAnim == 5) {
            i = spriteinstance.y;
            spriteinstance.y = 256 - this.cameraY - 15;
            if (this.bossRow() != 3) {
               break label53;
            }

            spriteinstance2 = spriteinstance;
            i1 = i;
         } else {
            spriteinstance2 = spriteinstance;
            i1 = 256 - i - this.cameraY;
         }

         spriteinstance2.y = i1;
      }

      spriteinstance.paint(graphics);
      if (this.bossSlamVisible) {
         spriteinstance1.y = 96 - this.cameraY;
         spriteinstance1.x = (this.bossColumn() + 1) * 24 - this.cameraX;
         spriteinstance1.paint(graphics);
      }

      for (int k = 0; k < 3; k++) {
         int j;
         if ((j = (k * (2 + (k > 0 ? 1 : 0)) + 10) * 24 - this.cameraX) < 320 && j > -48 && this.cameraY > 0) {
            sprites[40].paintFrame(graphics, 1, j, 216 - this.cameraY, 0, 0, 0);
         }
      }
   }

   private void paintStageAnimations() {
      int i = stateTicks;
      SpriteInstance spriteinstance = spriteInstances[2];
      if (this.cameraY + this.screenShakeOffset < 1008 && this.cameraY + this.screenShakeOffset > 672) {
         spriteinstance.x = 240 - this.cameraX;
         spriteinstance.y = 1008 - this.cameraY;
         spriteinstance.applyFrameStep();
         spriteinstance.paint(this.screen);
         spriteinstance.x = 336 - this.cameraX;
         spriteinstance.y = 1008 - this.cameraY;
         spriteinstance.applyFrameStep();
         spriteinstance.flags = 1;
         spriteinstance.paint(this.screen);
      }

      if (this.screenShakeTicks > 10) {
         GameCanvas gamecanvas = this;

         for (int b0 = 3; b0 < 13; b0 += 2) {
            int j;
            int k = ((j = 10 * (b0 * 2 / 5 + 1)) + stateTicks / j) * b0 % 320;
            j = 240 / j * stateTicks % 240;
            gamecanvas.screen.drawImage(imageSets[27][b0 & 1], k, j, 0);
         }
      }

      int j1 = this.mapHeight * 24 - this.floodHeight - this.cameraY;
      int k1;
      int l1 = k1 = 168 - this.cameraX;
      int i2 = k1 + 240;

      while (k1 <= -24) {
         k1 += 24;
      }

      SpriteInstance spriteinstance1 = spriteInstances[1];
      if ((this.floodHeight >= 816 || spriteinstance1.anim == 2) && this.floodHeight > 816) {
         for (int l = j1 + 20; l < 240; l += 24) {
            for (int i1 = k1; i1 < i2; i1 += 24) {
               spriteinstance1.sprite.paintAnimFrame(this.screen, 1, ((i >> 1) + i1 + l) % 2, i1, l, 0, 0, 0);
            }
         }
      }

      if (spriteinstance.anim == 2) {
         spriteinstance1.flags = 0;
         spriteinstance1.x = l1 + 120;
         spriteinstance1.y = j1;
         spriteinstance1.applyFrameStep();
         spriteinstance1.paint(this.screen);
         spriteinstance1.flags = 1;
         spriteinstance1.paint(this.screen);
         Graphics graphics = this.screen;
         i2 = (stateTicks << 3) % 160;
         l1 = (stateTicks / 160 & 1) == 0 ? 160 - i2 : i2 + 0;
         graphics.setColor(255, l1, 0);
         graphics.drawRect(0, 0, 319, 239);
      }
   }

   private void paintPlayer() {
      if (!this.cameraPanToCheckpoint) {
         int i = this.playerX;
         int j = this.playerY;
         int k = stateTicks;
         int l = this.playerFlags & 7;
         int i1 = (this.playerFlags & 16384) == 0 ? 0 : 3;
         SpriteInstance spriteinstance = spriteInstances[i1];
         boolean flag = this.isPlayerInsideBorder();
         int j1 = (this.playerFlags & 2048) == 0 ? this.paintPlayerDirection : this.playerFlags & 7;
         if (this.playerVisible) {
            this.playerScreenX = i * 24 + directionOffsets[j1] * this.moveProgress - this.cameraX;
            this.playerScreenY = j * 24 + directionOffsets[j1 + 8] * this.moveProgress - this.cameraY;
            if ((this.invulnerableTicks <= 0L || (k >> 1 & 1) == 0) && this.escapePressesLeft <= 0) {
               spriteinstance.x = this.playerScreenX;
               spriteinstance.y = this.playerScreenY;
               if (tileSlots != null && flag && l != 1 && l != 3 && tileSlots[i][j + 1] != 0 && isHeavyObject(i, j + 1)) {
                  int k1;
                  l = (k1 = (k >> 1) + i) % 4;
                  if ((k1 / 4 & 1) == 1) {
                     l = 4 - l;
                  }

                  spriteinstance.y += l;
               }

               spriteinstance.applyFrameStep();
               spriteinstance.paint(this.screen);
               k = spriteinstance.anim;
               pickupAnimStarting = spriteinstance.anim == 47 && spriteinstance.animFrame == 0;
               switch (k) {
                  case 17:
                  case 18:
                     if (spriteinstance.animFrame == 0) {
                        this.screen.drawImage(imageSets[this.pickupIconSet][this.pickupIconFrame], spriteinstance.x, spriteinstance.y - 12, 0);
                     }
                     break;
                  case 40:
                  case 47:
                  case 48:
                     if (spriteinstance.animFrame > (k == 40 ? 13 : 6) || k == 47) {
                        try {
                           byte b0 = 0;
                           if (this.pickupIconSet == 30 || this.pickupIconSet == 31 || this.pickupIconSet == 32) {
                              b0 = -2;
                           }

                           if (imageSets[this.pickupIconSet] == null) {
                              sprites[themedSpriteIndex(this.pickupIconSet)]
                                 .paintFrame(this.screen, this.pickupIconFrame, spriteinstance.x + b0, spriteinstance.y - 24, 0, 0, 0);
                           } else {
                              this.screen.drawImage(imageSets[this.pickupIconSet][this.pickupIconFrame], spriteinstance.x + b0, spriteinstance.y - 24, 0);
                           }
                        } catch (Exception exception1) {
                           System.out.println(exception1);
                        }

                        if (this.pickupValue > 0) {
                           drawNumber(this.screen, spriteinstance.x + 24, spriteinstance.y - 10, this.pickupValue, sprites[0].moduleImages[0], 0);
                        }
                     }
               }
            }

            if (flag && objectMap[i][j] == 0) {
               k = objectState[i][j] & 7;
               byte b1 = objectTimers[i][j];

               try {
                  sprites[themedSpriteIndex(1)]
                     .paintFrame(
                        this.screen,
                        0 + (objectState[i][j] & 56),
                        i * 24 - this.cameraX + directionOffsets[k] * b1,
                        j * 24 - this.cameraY + directionOffsets[k + 8] * b1,
                        0,
                        0,
                        0
                     );
                  return;
               } catch (Exception exception) {
               }
            }
         }
      }
   }

   private void computeObjectOffset() {
      int i = this.paintTileX;
      int j = this.paintTileY;
      int k = this.paintTileY + 1;
      this.overlayDx = 0;
      this.overlayDy = 0;
      this.overlayPending = false;
      int l;
      int i1 = (l = objectState[i][j]) & 7;
      byte b0 = objectTimers[i][j];
      if (l == -1) {
         l = -1;
      }

      this.paintOffsetX = this.paintOffsetX + b0 * directionOffsets[i1];
      this.paintOffsetY = this.paintOffsetY + b0 * directionOffsets[i1 + 8];
      boolean flag = tileSlots != null && tileSlots[i][j] != 0;
      switch (this.paintObject) {
         case 0:
            int l1 = (l & 56) >> 3;
            int j2 = (l & 28672) >> 12;
            boolean flag1 = (l & 65536) == 0;
            if (!flag && i1 == 0 && isHeavyObject(i, k)) {
               l = l & -28673 | j2 << 12;
               if (flag1) {
                  l &= -65537;
               } else {
                  l |= 65536;
                  j2 = -j2;
               }

               this.paintOffsetX = j2;
            } else {
               l &= -28673;
            }

            objectState[i][j] = l;
            this.paintOverlaySet = 1;
            this.paintOverlayFrame = l1 + 0;
            break;
         case 1:
            this.paintOverlaySet = 2;
            this.paintOverlayFrame = 0 + this.paintAnimPhase;
            break;
         case 2:
            GameCanvas gamecanvas = this;
            int j1 = tileMap[gamecanvas.paintTileX][gamecanvas.paintTileY] & 0xFF;
            int k1 = tileMap[gamecanvas.paintTileX][gamecanvas.paintTileY] >> 8;
            label113:
            if (j1 != 14 && j1 != 33 || k1 > 11) {
               gamecanvas.paintOverlaySet = 3;
               gamecanvas.paintOverlayFrame = 0 + gamecanvas.paintAnimPhase;
               GameCanvas gamecanvas2;
               Sprite[] asprite;
               byte b2;
               if (j1 == 14) {
                  gamecanvas2 = gamecanvas;
                  asprite = sprites;
                  b2 = 8;
               } else {
                  if (j1 != 33) {
                     break label113;
                  }

                  gamecanvas2 = gamecanvas;
                  asprite = sprites;
                  b2 = 22;
               }

               gamecanvas2.paintOffsetY = -(asprite[b2].getAnimFrameCount(0) - k1);
            }
            break;
         case 4:
            this.paintOverlaySet = 24;
            this.paintOverlayFrame = 0 + this.paintAnimPhase;
            break;
         case 5:
            this.paintOverlaySet = 25;
            this.paintOverlayFrame = 0 + this.paintAnimPhase;
            break;
         case 6:
            this.paintOverlaySet = 5;
            this.paintOverlayFrame = 0;
            break;
         case 7:
            this.paintOverlaySet = 5;
            this.paintOverlayFrame = 1;
            break;
         case 8:
            this.paintOverlaySet = 16;
            this.paintOverlayFrame = 0 + (stateTicks >> 1 & 1);
            break;
         case 9:
            paintSprite = sprites[(l & 264241152) >> 22];
            this.paintSpriteFrame = 0;
            break;
         case 47:
            paintSprite = sprites[30];
            this.paintSpriteAnim = 0;
            this.paintSpriteFrame = frameAtTime(paintSprite, 0, stateTicks % animLength(paintSprite, 0));
      }

      if (flag || tileSlots != null && tileSlots[i][k] != 0 && isHeavyObject(i, k)) {
         int i2;
         int k2 = (i2 = (stateTicks >> 1) + i) % 4;
         if ((i2 / 4 & 1) == 1) {
            k2 = 4 - k2;
         }

         this.paintOffsetY += k2;
      }

      if ((
            (l & 512) != 0
               || objectMap[i - directionOffsets[i1]][k] < 0 && isHeavyObject(i, k) && (objectState[i][k] & 7) == 0 && i != this.hookX && j != this.hookY
         )
         && (this.playerFlags & 8) == 0) {
         this.paintOffsetY += b0 * b0 / 24;
         if (this.paintObject != 9) {
            this.paintOffsetX = this.paintOffsetX + -1 + stateTicks % 3;
         }
      }

      if ((l & 512) != 0) {
         this.paintOffsetX = -this.paintOffsetX;
      }

      if (this.paintObject == 0) {
         this.overlayModule = ((l & 448) >> 6) - 1;
         if (this.overlayModule >= 0 && this.overlayModule < 5) {
            this.overlayPending = true;
            GameCanvas gamecanvas1;
            byte b1;
            switch (l & 7) {
               case 2:
                  gamecanvas1 = this;
                  b1 = -24;
                  break;
               case 4:
                  gamecanvas1 = this;
                  b1 = 24;
                  break;
               default:
                  gamecanvas1 = this;
                  b1 = 0;
            }

            gamecanvas1.overlayDx = b1;
            this.overlayDy = 13;
            this.overlayTileX = this.paintTileX;
            this.overlayTileY = this.paintTileY - 1;
         }
      }

      this.adjustForWindPod();
   }

   private void selectRollingObjectSprite(byte var1) {
      GameCanvas gamecanvas;
      int k;
      Sprite sprite;
      int l;
      label33: {
         int i = objectState[this.paintTileX][this.paintTileY];
         byte b0 = objectTimers[this.paintTileX][this.paintTileY];
         int j = i & 7;
         this.paintOffsetX = b0 * directionOffsets[j];
         this.paintOffsetY = b0 * directionOffsets[j + 8];
         var1 = (byte)(var1 == 19 ? 4 : 21);
         paintSprite = sprites[var1];
         if (this.world == 1) {
            byte b1;
            if ((i & 248) >> 3 > 0) {
               gamecanvas = this;
               b1 = 2;
            } else {
               gamecanvas = this;
               b1 = 0;
            }

            gamecanvas.paintSpriteAnim = b1;
            gamecanvas = this;
            k = stateTicks;
            sprite = paintSprite;
         } else {
            label30: {
               if ((i & 248) >> 3 > 0) {
                  gamecanvas = this;
                  k = 4;
               } else {
                  this.paintSpriteAnim = j - 1;
                  if (this.paintSpriteAnim >= 0) {
                     break label30;
                  }

                  gamecanvas = this;
                  k = ((i & 28672) >> 12) - 1;
               }

               gamecanvas.paintSpriteAnim = k;
            }

            gamecanvas = this;
            k = stateTicks >> 1;
            sprite = paintSprite;
            if (this.paintSpriteAnim < 0) {
               l = 0;
               break label33;
            }
         }

         l = this.paintSpriteAnim;
      }

      gamecanvas.paintSpriteFrame = k % sprite.getAnimFrameCount(l);
      this.adjustForWindPod();
   }

   private static int frameAtTime(Sprite var0, int var1, int var2) {
      int i = 0;

      for (int j = 0; var2 > 0; i = j++) {
         var2 -= var0.getAnimFrameDuration(var1, j);
      }

      return i;
   }

   private static int firstFrameDuration(Sprite var0, int var1, int var2) {
      var2 = 0;

      for (int i = 0; i < 1; i++) {
         var2 = 0 + var0.getAnimFrameDuration(var1, 0);
      }

      return var2;
   }

   private static void drawNumber(Graphics var0, int var1, int var2, int var3, Image[] var4, int var5) {
      if (var3 == 0) {
         Image image1 = sprites[0].moduleImages[0][0];
         var0.drawImage(image1, var1, var2, 24);
      } else {
         while (var3 > 0) {
            var5 = var3 % 10;
            var3 /= 10;
            Image image = var4[var5];
            var1 -= image.getWidth();
            var0.drawImage(image, var1, var2, 0);
         }
      }
   }

   private boolean isPlayerInsideBorder() {
      return this.playerX > 0 && this.playerX < this.mapWidth - 1 && this.playerY > 0 && this.playerY < this.mapHeight - 1;
   }

   private void setPlayerAnim(int var1) {
      int i = spriteInstances[0].anim;
      if (spriteInstances[0].anim != 19 && (this.playerFlags & 16384) == 0 && (this.playerFlags & 2048) == 0) {
         if (this.isPlayerInsideBorder()) {
            int j;
            if ((j = tileSlots == null ? 0 : slotBits(tileSlots[this.playerX][this.playerY], (byte)0, (byte)3, (byte)4)) != 8 && j != 7) {
               label49:
               if (objectMap[this.playerX][this.playerY + 1] < 0 || objectMap[this.playerX][this.playerY + 1] == 14) {
                  byte b0;
                  switch (var1) {
                     case 0:
                        b0 = 0;
                        break;
                     case 1:
                        b0 = 35;
                        break;
                     case 2:
                        b0 = 2;
                        break;
                     case 3:
                        b0 = 34;
                        break;
                     case 4:
                     case 5:
                     case 6:
                     case 7:
                     case 8:
                     case 9:
                     case 10:
                     case 11:
                     case 12:
                     case 13:
                     case 15:
                     case 17:
                     case 18:
                     case 19:
                     case 24:
                     case 25:
                     case 26:
                     case 27:
                     case 28:
                     case 29:
                     case 30:
                     case 31:
                     case 32:
                     case 33:
                     case 34:
                     case 35:
                     case 36:
                     case 37:
                     case 38:
                     case 39:
                     case 40:
                     case 41:
                     case 43:
                     default:
                        break label49;
                     case 14:
                        b0 = 28;
                        break;
                     case 16:
                        b0 = 29;
                        break;
                     case 20:
                        b0 = 30;
                        break;
                     case 21:
                        b0 = 32;
                        break;
                     case 22:
                        b0 = 31;
                        break;
                     case 23:
                        b0 = 33;
                        break;
                     case 42:
                        b0 = 46;
                        break;
                     case 44:
                        b0 = 45;
                  }

                  var1 = b0;
               }
            } else {
               switch (var1) {
                  case 0:
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                  case 7:
                  case 8:
                  case 9:
                  case 11:
                  case 24:
                  case 25:
                  case 26:
                  case 27:
                     var1 = 36 + (this.playerFlags & 7) - 1;
                  case 10:
                  case 12:
                  case 13:
                  case 14:
                  case 15:
                  case 16:
                  case 17:
                  case 18:
                  case 19:
                  case 20:
                  case 21:
                  case 22:
                  case 23:
               }
            }
         }

         if (var1 == 1000) {
            spriteInstances[3].setAnim(0);
            this.playerFlags |= 16384;
         } else {
            spriteInstances[0].setAnim(var1);
         }

         if (i != var1) {
            this.idleAnimDelay = 70;
         }
      }
   }

   private void updateShop() {
      if (isKeyDown(32944)) {
         keysDown = 0;
         this.fullRedraw = true;
         int i = this.armorLevel;
         GameCanvas gamecanvas1 = this;
         this.shownArmorLevel = -1;
         switch (armorStatus(i)) {
            case -1:
               gamecanvas1.shopMessage = strings[41];
               return;
            case 0:
               gamecanvas1.shopMessage = strings[0];
               return;
            case 1:
               gamecanvas1.stageDiamonds = readShort(saveData, 4) - Constants.armorDiamondThresholds[gamecanvas1.armorLevel];
               gamecanvas1.shopDiamondsText = null;
               System.gc();
               gamecanvas1.shopDiamondsText = strings[125] + " " + gamecanvas1.stageDiamonds + " " + strings[19];
               saveData[4] = (byte)gamecanvas1.stageDiamonds;
               saveData[5] = (byte)(gamecanvas1.stageDiamonds >> 8);
               saveData[8] = (byte)(i + 4 + 1);
               gamecanvas1.writeSave();
               gamecanvas1.nextArmorThreshold = -1;
               gamecanvas1.shopMessage = strings[0];
               gamecanvas1.shopTextChanged = true;
               gamecanvas1.showNotice(strings[32], -1, -1, 5000, 4273165, 0);
         }
      } else if (isKeyDown(64)) {
         keysDown = 0;
         gameState = 27;
         this.fullRedraw = true;
         this.sealDirty = true;
         keysDown = 0;
      } else {
         label41: {
            GameCanvas gamecanvas3;
            byte b1;
            if (isKeyDown(4097)) {
               gamecanvas3 = this;
               b1 = -1;
            } else {
               if (!isKeyDown(262146)) {
                  break label41;
               }

               gamecanvas3 = this;
               b1 = 1;
            }

            byte b0 = b1;
            GameCanvas gamecanvas = gamecanvas3;
            gamecanvas3.armorLevel += b0;
            if (gamecanvas.armorLevel < 0) {
               gamecanvas.armorLevel = 3;
            }

            if (gamecanvas.armorLevel == 4) {
               gamecanvas.armorLevel = 0;
            }
         }

         keysDown = 0;
         GameCanvas gamecanvas2;
         String[] astring;
         byte b2;
         if (armorStatus(this.armorLevel) == 0) {
            this.nextArmorThreshold = -1;
            gamecanvas2 = this;
            astring = strings;
            b2 = 0;
         } else {
            this.nextArmorThreshold = Constants.armorDiamondThresholds[this.armorLevel];
            this.armorThresholdText.delete(0, this.armorThresholdText.length());
            this.armorThresholdText.append(this.nextArmorThreshold);
            gamecanvas2 = this;
            astring = strings;
            b2 = 6;
         }

         gamecanvas2.shopMessage = astring[b2];
         this.fullRedraw = true;
      }
   }

   private static int armorStatus(int var0) {
      if (saveData[8] >= var0 + 4 + 1) {
         return 0;
      } else {
         return readShort(saveData, 4) < Constants.armorDiamondThresholds[var0] ? -1 : 1;
      }
   }

   private void startNewGame() {
      this.creatingNewGame = true;
      gameState = 16;
      this.loadingStep = 0;
      this.loadingStepCount = 6;
      this.lives = 0;
      this.stageRedDiamonds = 0;
      this.stageDiamonds = 0;
      this.hasCompass = false;
      this.tipIndex = 0;
      unlockedStage = 0;
   }

   private void checkCheatCode() {
      if (cheatCodeProgress < cheatCodeKeys.length && isKeyDown(cheatCodeKeys[cheatCodeProgress])) {
         keysDown = 0;
         if (++cheatCodeProgress == cheatCodeKeys.length) {
            keysDown = 0;
            gameState = 24;
            this.cheatMaxEnergy = saveData[8];
            this.cheatWeapons = saveData[9];
            this.cheatUnlockAllStages = false;
            this.cheatUnlockSecretStages = false;
            return;
         }
      } else {
         byte b0;
         if (isKeyDown(cheatCodeKeys[0])) {
            keysDown = 0;
            b0 = 1;
         } else {
            b0 = 0;
         }

         cheatCodeProgress = b0;
      }
   }

   private void closeCheatMenu() {
      this.screen.setClip(0, 0, 320, 240);
      this.screen.setColor(0);
      this.screen.fillRect(0, 0, 320, 240);
      gameState = stateBeforeCheats;
      if (stateBeforeCheats == 27) {
         this.fullRedraw = true;
         this.sealDirty = true;
      } else if (stateBeforeCheats == 1) {
         this.hudDirty = true;
      }

      keysDown = 0;
   }

   private void updateMenu() {
      if (this.menuAnimFrame >= 0 && this.menuAnimFrame <= 2) {
         keysDown = 0;
      } else {
         if (isKeyDown(4097)) {
            if (System.currentTimeMillis() < 300L) {
               return;
            }

            this.menuUp();
            if (this.menu == 0 && this.menuSelection == 0 && !this.menuGameStarted) {
               this.menuUp();
            }
         } else if (isKeyDown(262146)) {
            if (System.currentTimeMillis() < 300L) {
               return;
            }

            this.menuDown();
            if (this.menu == 0 && this.menuSelection == 0 && !this.menuGameStarted) {
               this.menuDown();
            }
         } else if (isKeyDown(32944)) {
            if (this.menuAnimFrame < 0 || this.menuAnimFrame > 2) {
               this.menuAnimFrame = 0;
               this.menuAnimTime = System.currentTimeMillis();
            }
         } else if (isKeyDown(64)) {
            switch (this.menu) {
               case -1:
               case 0:
               case 1:
               case 3:
               default:
                  break;
               case 2:
                  gameState = 9;
                  this.openMenu(0);
                  this.loadingStepCount = 8;
                  this.loadingStep = 0;
                  break;
               case 4:
                  this.openMenu(2);
                  break;
               case 5:
                  menuNeedsRedraw = true;
                  pauseBackgroundStale = false;
                  if (gameState == 2) {
                     this.openMenu(1);
                     this.hudDirty = true;
                     pauseBackgroundStale = true;
                  }

                  if (gameState == 4) {
                     this.openMenu(0);
                     this.playSound(19);
                  }
            }
         }

         keysDown = 0;
      }
   }

   private boolean tryStartFloorToggle() {
      if (this.enemyPhase == 3) {
         int i = tileMap[this.playerX][this.playerY] & 0xFF;
         if (this.toggleDirection == 0 && i != 15 && i != 16) {
            this.playSound(0);
            GameCanvas gamecanvas;
            byte b0;
            if (this.togglePhase <= 0) {
               gamecanvas = this;
               b0 = 1;
            } else {
               gamecanvas = this;
               b0 = -1;
            }

            gamecanvas.toggleDirection = b0;
         }

         return true;
      } else {
         return false;
      }
   }

   private void hitObject(int var1, int var2, int var3) {
      int i = objectState[var2][var3];
      if (var1 == 43 && (i & 248) == 0) {
         if ((i & 98304) == 0) {
            objectMap[var2][var3] = -1;
            this.triggerTile(var2, var3);
            return;
         }

         int j;
         i = ((j = (i - 32768 & -16646145 | var2 << 17) & -2130706433 | var3 << 24) & 7) != 1 && (j & 7) != 3 ? j & 2147483647 : j | -2147483648;
      }

      i = i & -249 | 120;
      objectState[var2][var3] = i;
   }

   private void smashObject(int var1, int var2) {
      switch (objectMap[var1][var2]) {
         case 0:
            this.playSound(11);
         case 19:
         case 43:
         case 45:
         case 46:
            tileMap[var1][var2] = -1;
            this.triggerTile(var1, var2);
            return;
         case 48:
            tileMap[var1][var2] = -1;
            this.triggerTile(var1, var2);
            int i = var2 + 1;
            if (objectMap[var1][i] != 48) {
               i = -1;
            }

            objectMap[var1][i] = -1;
            this.triggerTile(var1, i);
            int j = (objectState[var1][var2] >> 24) * 3;
            collapsingRows[j + 2] = -1;
            return;
         default:
            tileMap[var1][var2] = -1;
      }
   }

   private void updatePlaying() {
      if (this.underHeavyObject) {
         this.crushTimer--;
      }

      if ((this.crystalExitPending || this.bossExitPending) && stateTicks > 140) {
         if (this.bossExitPending) {
            this.bankStageDiamonds();
         }

         this.crystalExitPending = false;
         this.bossExitPending = false;
         this.unloadCutscenesOnLoad = true;
         this.loadingStep = 0;
         this.resetStateTimer();
         this.saveResultOnLoad = true;
         gameState = 28;
      }

      boolean flag = this.isPlayerInsideBorder();
      if (this.enemyRemovalPending) {
         this.enemyRemovalPending = false;
         if (this.enemyRemovalIndex < this.enemyCount) {
            int i = enemyBits((byte)this.enemyRemovalIndex, (byte)13, (byte)7);
            int j = enemyBits((byte)this.enemyRemovalIndex, (byte)20, (byte)7);
            this.killEnemyAt(i, j);
            this.enemyRemovalIndex++;
         } else {
            this.enemyRemovalIndex = 0;
            this.enemyCount = 0;
         }
      }

      if (spriteInstances[4] != null) {
         spriteInstances[4].updateAnim();
      }

      if (this.screenShakeTicks > 0) {
         this.screenShakeTicks--;
      }

      if (this.hintTextTicks > 0) {
         this.hintTextTicks--;
         if (this.hintTextTicks == 0) {
            this.hintText = null;
         }
      }

      if (this.drawnEffect != this.nextEffect) {
         int l11 = this.drawnEffect;

         while (true) {
            int k2 = l11;
            if (l11 == this.nextEffect) {
               break;
            }

            effectTimers[k2]++;
            int k3 = effectTypes[k2] < 0 ? animLength(sprites[2], 0) : sprites[9].getAnimFrameCount(effectTypes[k2]);
            if (effectTimers[k2] >= k3) {
               this.drawnEffect++;
               this.drawnEffect &= 7;
            }

            l11 = k2 + 1 & 7;
         }
      }

      if (this.stageKind != 0) {
         GameCanvas gamecanvas = this;
         label1368:
         switch (this.stageKind) {
            case 1:
               GameCanvas gamecanvas6 = gamecanvas;
               if (objectMap[18][63] == 0 && objectTimers[18][63] <= 0 && gamecanvas6.floodSwitchPresses == 0) {
                  gamecanvas6.screenShakeTicks = 120;
                  gamecanvas6.floodSwitchPresses++;
               }

               label1365: {
                  SpriteInstance spriteinstance6;
                  byte b19;
                  if (gamecanvas6.floodSwitchPresses == 3) {
                     if (spriteInstances[2].anim != 0) {
                        if (spriteInstances[2].anim == 1 && spriteInstances[2].isAnimFinished()) {
                           spriteInstances[2].setAnim(2);
                           if (gamecanvas6.playerY == gamecanvas6.mapHeight - 4) {
                              gamecanvas6.floodHeight = 817;
                           }
                        }
                        break label1365;
                     }

                     spriteinstance6 = spriteInstances[2];
                     b19 = 1;
                  } else {
                     if (spriteInstances[2].anim == 0) {
                        break label1365;
                     }

                     spriteinstance6 = spriteInstances[2];
                     b19 = 0;
                  }

                  spriteinstance6.setAnim(b19);
               }

               spriteInstances[2].updateAnim();
               if (spriteInstances[2].anim != 2) {
                  break;
               }

               spriteInstances[1].updateAnim();
               if (gamecanvas6.screenShakeTicks == 10) {
                  gamecanvas6.screenShakeTicks = 60;
               }

               if (spriteInstances[1].anim != 0) {
                  if (spriteInstances[1].isAnimFinished()) {
                     spriteInstances[1].setAnim(0);
                  }
               } else {
                  if ((gamecanvas6.activeCutscene == null || gamecanvas6.floodHeight < 46) && gamecanvas6.floodHeight < 1704) {
                     gamecanvas6.floodHeight++;
                     int k5 = gamecanvas6.mapHeight * 24 - (gamecanvas6.cameraY + 240 - 62);
                     if (gamecanvas6.floodHeight < k5) {
                        gamecanvas6.floodHeight = k5;
                     }
                  }

                  if (gamecanvas6.mapHeight * 24 - gamecanvas6.floodHeight <= gamecanvas6.playerY * 24 + 18 && gamecanvas6.playerX < 17) {
                     gamecanvas6.hurtPlayer(saveData[8], 64, 1);
                  }
               }
               break;
            case 2:
               GameCanvas gamecanvas5 = gamecanvas;
               if (tileMap != null && gamecanvas5.isPlayerInsideBorder() && (tileMap[gamecanvas5.playerX][gamecanvas5.playerY] & 0xFF) == 0) {
                  int j5;
                  if ((j5 = tileMap[gamecanvas5.playerX][gamecanvas5.playerY] >> 8) == 13) {
                     gamecanvas5.prologueSaw13 = true;
                  } else if (j5 == 16) {
                     gamecanvas5.prologueSaw16 = true;
                  }
               }

               if (!gamecanvas5.prologueMarkerReached && gamecanvas5.moveProgress <= 0 && gamecanvas5.isPlayerAt(46, 7)) {
                  gamecanvas5.prologueMarkerHint = true;
                  gamecanvas5.prologueMarkerReached = true;
               }

               gamecanvas5 = gamecanvas;
               if (gamecanvas.isPlayerAt(61, 3) && gamecanvas5.moveProgress == 6) {
                  gamecanvas5.addEffect(gamecanvas5.playerX, gamecanvas5.playerY, 5);
                  gamecanvas5.playerVisible = false;
               }

               if (gamecanvas5.activeCutscene == null && (gamecanvas5.isPlayerAt(60, 3) || gamecanvas5.isPlayerAt(61, 3))) {
                  gamecanvas5.markGameStarted(true);
                  gamecanvas5.writeSaveOnLoad = true;
                  gamecanvas5.unloadMapResources();
                  gamecanvas5.freeStageResources();
                  gamecanvas5.world = 0;
                  gamecanvas5.stage = 0;
                  gamecanvas5.startStage();
               }

               if (gamecanvas5.pickupObject == 2) {
                  tileMap[11][50] = -1;
               }
               break;
            case 3:
               GameCanvas gamecanvas4 = gamecanvas;
               SpriteInstance spriteinstance2 = spriteInstances[5];
               int l2 = gamecanvas4.scanX;
               int i7 = gamecanvas4.scanY;
               int k7 = gamecanvas4.playerX - 8;
               int k8 = gamecanvas4.playerX + 8;
               int j9 = gamecanvas4.playerY + 8;
               int l9 = gamecanvas4.playerY - 8;

               for (int j10 = 15; j10 <= 22; j10++) {
                  for (int k10 = 14; k10 <= 21; k10++) {
                     if ((k10 <= k7 || k10 >= k8 || j10 <= j9 || j10 >= l9) && objectMap[k10][j10] == 44) {
                        gamecanvas4.scanX = k10;
                        gamecanvas4.scanY = j10;
                        gamecanvas4.updateStalactite();
                     }
                  }
               }

               gamecanvas4.scanX = l2;
               gamecanvas4.scanY = i7;
               if (gamecanvas4.energy == 0) {
                  gamecanvas4.resetBoss();
               }

               SpriteInstance spriteinstance5;
               if (gamecanvas4.bossAnim == 12) {
                  if (gamecanvas4.bossTimer++ > 100) {
                     gamecanvas4.bossAnim = 15;
                     gamecanvas4.triggerTile(11, 11);
                  } else {
                     gamecanvas4.sound.play(7);
                  }

                  spriteinstance5 = spriteinstance2;
               } else {
                  if (gamecanvas4.bossAnim == -1) {
                     if (gamecanvas4.playerX * 24 >= 336) {
                        gamecanvas4.bossAnim = 0;
                        spriteinstance2.setAnim(0);
                     }
                     break;
                  }

                  if (gamecanvas4.bossAnim == 15 || gamecanvas4.bossAnim == -1) {
                     break;
                  }

                  byte b9 = -1;
                  int l10 = gamecanvas4.playerX * 24 + 12;
                  k8 = gamecanvas4.playerY * 24;
                  j9 = gamecanvas4.yetiX + 24;
                  if (gamecanvas4.yetiNextRoar == 0L && gamecanvas4.energy != 0) {
                     gamecanvas4.yetiNextRoar = stateTicks + Rand.between(340, 441);
                  }

                  l9 = gamecanvas4.bossHealth > 0 && stateTicks % gamecanvas4.bossHealth == 0 ? 2 : 1;
                  if (gamecanvas4.bossAnim == 10 || gamecanvas4.bossAnim == 11) {
                     l9 = (stateTicks & 11) == 0 ? 2 : l9;
                  }

                  while (l9-- > 0) {
                     switch (gamecanvas4.bossAnim) {
                        case 0:
                           if (j9 > 360) {
                              b9 = 4;
                              gamecanvas4.bossAnim = 4;
                           } else {
                              b9 = 5;
                              gamecanvas4.bossAnim = 5;
                           }
                           break;
                        case 1:
                           if (j9 < 504) {
                              b9 = 5;
                              gamecanvas4.bossAnim = 5;
                           } else {
                              b9 = 4;
                              gamecanvas4.bossAnim = 4;
                           }
                           break;
                        case 2:
                           if (spriteinstance2.isAnimFinished()) {
                              if (stateTicks > gamecanvas4.yetiNextRoar) {
                                 b9 = 13;
                                 gamecanvas4.bossAnim = 13;
                              } else {
                                 b9 = 4;
                                 gamecanvas4.bossAnim = 4;
                              }
                           }
                           break;
                        case 3:
                           if (spriteinstance2.isAnimFinished()) {
                              if (stateTicks > gamecanvas4.yetiNextRoar) {
                                 b9 = 14;
                                 gamecanvas4.bossAnim = 14;
                              } else {
                                 b9 = 5;
                                 gamecanvas4.bossAnim = 5;
                              }
                           }
                           break;
                        case 4:
                           if (stateTicks > gamecanvas4.yetiNextRoar) {
                              b9 = 13;
                              gamecanvas4.bossAnim = 13;
                           } else {
                              if (k8 >= 504 && l10 < j9 && gamecanvas4.yetiX - 48 >= 360) {
                                 b9 = 10;
                                 gamecanvas4.bossAnim = 10;
                              }

                              if (j9 <= 360) {
                                 b9 = 5;
                                 gamecanvas4.bossAnim = 5;
                              }

                              if (gamecanvas4.bossAnim == 4) {
                                 gamecanvas4.yetiX--;
                              }
                           }
                           break;
                        case 5:
                           if (stateTicks > gamecanvas4.yetiNextRoar) {
                              b9 = 14;
                              gamecanvas4.bossAnim = 14;
                           } else {
                              if (k8 >= 504 && l10 > j9 && gamecanvas4.yetiX + 48 <= 504) {
                                 b9 = 11;
                                 gamecanvas4.bossAnim = 11;
                              }

                              if (j9 >= 504) {
                                 b9 = 4;
                                 gamecanvas4.bossAnim = 4;
                              }

                              if (gamecanvas4.bossAnim == 5) {
                                 gamecanvas4.yetiX++;
                              }
                           }
                           break;
                        case 6:
                        case 7:
                        case 13:
                        case 14:
                           byte b11;
                           byte b2;
                           byte b4;
                           label1305: {
                              b11 = 0;
                              b2 = 0;
                              b4 = 0;
                              byte b13;
                              switch (gamecanvas4.bossAnim) {
                                 case 6:
                                 case 13:
                                    b11 = 4;
                                    b2 = 2;
                                    b13 = 1;
                                    break;
                                 case 7:
                                 case 14:
                                    b11 = 5;
                                    b2 = 1;
                                    b13 = 2;
                                    break;
                                 case 8:
                                 case 9:
                                 case 10:
                                 case 11:
                                 case 12:
                                 default:
                                    break label1305;
                              }

                              b4 = b13;
                           }

                           if (spriteinstance2.animFrame == 5 && spriteinstance2.frameTimer == 0) {
                              GameCanvas gamecanvas11;
                              byte b16;
                              if (gamecanvas4.bossAnim != 13 && gamecanvas4.bossAnim != 14) {
                                 gamecanvas11 = gamecanvas4;
                                 b16 = 10;
                              } else {
                                 gamecanvas11 = gamecanvas4;
                                 b16 = 80;
                              }

                              gamecanvas11.screenShakeTicks = b16;
                           }

                           if (spriteinstance2.isAnimFinished()) {
                              if (gamecanvas4.bossAnim == 13 || gamecanvas4.bossAnim == 14) {
                                 gamecanvas4.icicleDropTime = stateTicks + 40;
                                 gamecanvas4.yetiNextRoar = stateTicks + Rand.between(340, 441);
                              }

                              b9 = b11;
                              gamecanvas4.bossAnim = b11;
                           }

                           if (spriteinstance2.animFrame >= 5 && k8 >= 504 && l10 >= j9 - b2 * 24 && l10 <= j9 + b4 * 24) {
                              gamecanvas4.hurtPlayer(1, 48, 4);
                           }
                        case 8:
                        case 9:
                        case 12:
                        default:
                           break;
                        case 10:
                           if (spriteinstance2.isAnimFinished()) {
                              gamecanvas4.screenShakeTicks = 10;
                              j9 = gamecanvas4.yetiX + 24;
                              if (stateTicks > gamecanvas4.yetiNextRoar) {
                                 b9 = 13;
                                 gamecanvas4.bossAnim = 13;
                              } else if (k8 >= 504) {
                                 if (l10 >= j9 - 48) {
                                    b9 = 6;
                                    gamecanvas4.bossAnim = 6;
                                 }
                              } else {
                                 b9 = 4;
                                 gamecanvas4.bossAnim = 4;
                              }
                           } else {
                              gamecanvas4.yetiX -= 2;
                           }
                           break;
                        case 11:
                           if (spriteinstance2.isAnimFinished()) {
                              gamecanvas4.screenShakeTicks = 10;
                              j9 = gamecanvas4.yetiX + 24;
                              if (stateTicks > gamecanvas4.yetiNextRoar) {
                                 b9 = 14;
                                 gamecanvas4.bossAnim = 14;
                              } else if (k8 >= 504) {
                                 if (l10 <= j9 + 48) {
                                    b9 = 7;
                                    gamecanvas4.bossAnim = 7;
                                 }
                              } else {
                                 b9 = 5;
                                 gamecanvas4.bossAnim = 5;
                              }
                           } else {
                              gamecanvas4.yetiX += 2;
                           }
                     }

                     if (k8 >= 504 && l10 >= j9 - 24 && l10 <= j9 + 24) {
                        gamecanvas4.hurtPlayer(1, 48, oppositeDirection[gamecanvas4.playerFlags & 7]);
                     }

                     if (gamecanvas4.bossHealth <= 0) {
                        gamecanvas4.bossAnim = 12;
                        gamecanvas4.bossTimer = 0;
                        break label1368;
                     }
                  }

                  label1272:
                  if (gamecanvas4.togglePhase == 5) {
                     int[] aint;
                     byte b17;
                     if (gamecanvas4.toggleDirection <= 0) {
                        if (gamecanvas4.bossHealth <= 2) {
                           objectMap[10][16] = 45;
                           tileRedrawTimers[10][16] = 24;
                           objectTimers[10][16] = 0;
                           objectState[10][16] = 0;
                        }

                        objectMap[26][19] = 45;
                        tileRedrawTimers[26][19] = 24;
                        objectTimers[26][19] = 0;
                        aint = objectState[26];
                        b17 = 19;
                     } else {
                        objectMap[10][19] = 45;
                        tileRedrawTimers[10][19] = 24;
                        objectTimers[10][19] = 0;
                        objectState[10][19] = 0;
                        if (gamecanvas4.bossHealth > 2) {
                           break label1272;
                        }

                        objectMap[26][16] = 45;
                        tileRedrawTimers[26][16] = 24;
                        objectTimers[26][16] = 0;
                        aint = objectState[26];
                        b17 = 16;
                     }

                     aint[b17] = 0;
                  }

                  if (gamecanvas4.bossAnim != 10 && gamecanvas4.bossAnim != 11) {
                     for (int l8 = 21; l8 < 23; l8++) {
                        for (int i11 = j9 / 24 - 1; i11 < j9 / 24 - 2 + 4; i11++) {
                           if (objectMap[i11][l8] == 9) {
                              label1255:
                              if ((objectState[i11][l8] & 7) == 3) {
                                 gamecanvas4.bossHealth--;
                                 GameCanvas gamecanvas12;
                                 byte b18;
                                 switch (gamecanvas4.bossAnim) {
                                    case 0:
                                    case 2:
                                    case 4:
                                    case 6:
                                       gamecanvas12 = gamecanvas4;
                                       b18 = 2;
                                       break;
                                    case 1:
                                    case 3:
                                    case 5:
                                    case 7:
                                       gamecanvas12 = gamecanvas4;
                                       b18 = 3;
                                       break;
                                    default:
                                       break label1255;
                                 }

                                 b9 = b18;
                                 gamecanvas12.bossAnim = b18;
                              }

                              objectMap[i11][l8] = -1;
                              gamecanvas4.refreshBackgroundTile(i11, l8);
                              objectMap[i11][l8] = 30;
                              tileRedrawTimers[i11][l8] = 24;
                              objectState[i11][l8] = 4;
                              gamecanvas4.playSound(14);
                           }
                        }
                     }
                  }

                  if (b9 != -1) {
                     spriteinstance2.setAnim(b9);
                     break;
                  }

                  spriteinstance5 = spriteinstance2;
               }

               spriteinstance5.updateAnim();
               break;
            case 4:
               GameCanvas gamecanvas3 = gamecanvas;
               if (gamecanvas.bossAnim == 5) {
                  if (gamecanvas3.arenaWallsRestored) {
                     break;
                  }

                  gamecanvas3.arenaWallsRestored = true;
               }

               SpriteInstance spriteinstance1 = spriteInstances[5];
               gamecanvas3.bossTimer++;
               byte b1 = -1;
               SpriteInstance spriteinstance3 = spriteInstances[4];
               byte b6 = -1;
               int j8 = gamecanvas3.bossColumn();
               if (objectMap[gamecanvas3.arenaWallX1][2] == -1) {
                  objectMap[gamecanvas3.arenaWallX1][2] = 31;
               }

               if (objectMap[gamecanvas3.arenaWallX2][2] == -1) {
                  objectMap[gamecanvas3.arenaWallX2][2] = 31;
               }

               switch (gamecanvas3.bossAnim) {
                  case 0:
                     if (gamecanvas3.playerX >= 10) {
                        gamecanvas3.bossAnim = 6;
                        gamecanvas3.bossTimer = 0;
                     }
                     break;
                  case 1:
                     if (gamecanvas3.bossTimer > 40) {
                        gamecanvas3.bossAnim = 2;
                        gamecanvas3.bossTimer = 0;
                     } else if (gamecanvas3.bossTimer > 20) {
                        gamecanvas3.crushObjectsAt(j8, 8);
                        fillObjectPair(j8, 8);
                        gamecanvas3.bossHeldObjectX = j8;
                        gamecanvas3.bossHeldObjectY = 8;
                     }
                     break;
                  case 2:
                     if (gamecanvas3.crushObjectsAt(j8, 8)) {
                        gamecanvas3.bossHealth--;
                        gamecanvas3.bossPreviousAnim = gamecanvas3.bossAnim;
                        clearObjectPair(gamecanvas3.bossHeldObjectX, gamecanvas3.bossHeldObjectY);
                        gamecanvas3.bossHeldObjectX = -1;
                        gamecanvas3.bossHeldObjectY = -1;
                        gamecanvas3.bossPreviousAnim = gamecanvas3.bossAnim;
                        gamecanvas3.bossAnim = 3;
                        b1 = 3;
                        gamecanvas3.playSound(10);
                     }

                     if (gamecanvas3.bossTimer > 15 && spriteinstance1.anim != 6) {
                        b1 = 6;
                     }

                     if (gamecanvas3.bossTimer > 30) {
                        gamecanvas3.bossAnim = 4;
                        gamecanvas3.bossTimer = 0;
                        b1 = 0;
                        clearObjectPair(gamecanvas3.bossHeldObjectX, gamecanvas3.bossHeldObjectY);
                        gamecanvas3.bossHeldObjectX = -1;
                        gamecanvas3.bossHeldObjectY = -1;
                     }
                     break;
                  case 3:
                     if (gamecanvas3.bossTimer > 40) {
                        if (gamecanvas3.bossHealth > 0) {
                           if (gamecanvas3.bossPreviousAnim == 10) {
                              gamecanvas3.bossAnim = 9;
                              gamecanvas3.bossTimer = 0;
                           } else if (gamecanvas3.bossPreviousAnim == 2 || gamecanvas3.bossPreviousAnim == 1) {
                              gamecanvas3.bossAnim = 4;
                              gamecanvas3.bossTimer = 0;
                              b1 = 2;
                           }
                        } else {
                           gamecanvas3.bossAnim = 7;
                           gamecanvas3.bossTimer = 0;
                        }
                     }
                     break;
                  case 4:
                     label1224: {
                        int k9 = gamecanvas3.bossHealth <= 1 ? 5 : 10;
                        byte b12;
                        if (gamecanvas3.bossTimer >= k9) {
                           gamecanvas3.bossAnim = 5;
                           gamecanvas3.bossTimer = 0;
                           b12 = 4;
                        } else {
                           if (gamecanvas3.bossTimer <= k9 >> 1 || spriteinstance1.anim == 1) {
                              break label1224;
                           }

                           b12 = 1;
                        }

                        b1 = b12;
                     }

                     gamecanvas3.crushObjectsUnderBoss();
                     gamecanvas3.checkBossContact();
                     break;
                  case 5:
                     int i10 = spriteinstance1.y + -40;
                     int i2 = (spriteinstance1.sprite.animFrameStarts[spriteinstance1.anim] + spriteinstance1.animFrame) * 5;
                     byte b10 = spriteinstance1.sprite.animFrames[i2 + 3];
                     if (i10 - b10 <= 72 - gamecanvas3.cameraY + 40) {
                        gamecanvas3.unusedBossValue = 0;
                        gamecanvas3.restoreArenaWalls();
                        gamecanvas3.bossAnim = 10;
                        gamecanvas3.bossHeldObjectX = j8;
                        gamecanvas3.bossHeldObjectY = 4;
                        fillObjectPair(gamecanvas3.bossHeldObjectX, gamecanvas3.bossHeldObjectY);
                     }

                     gamecanvas3.crushObjectsUnderBoss();
                     gamecanvas3.checkBossContact();
                     break;
                  case 6:
                     if (gamecanvas3.bossTimer > 10) {
                        gamecanvas3.bossAnim = 1;
                        b1 = 2;
                        gamecanvas3.bossTimer = 0;
                     }
                     break;
                  case 7:
                     clearObjectPair(gamecanvas3.bossHeldObjectX, gamecanvas3.bossHeldObjectY);
                     gamecanvas3.bossHeldObjectX = -1;
                     gamecanvas3.bossHeldObjectY = -1;
                     if (gamecanvas3.bossTimer > 80) {
                        gamecanvas3.bossAnim = 8;
                        gamecanvas3.pressActiveSwitch();
                     }

                     if ((gamecanvas3.bossTimer & 111) == 1) {
                        gamecanvas3.playSound(7);
                     }
                  case 8:
                  default:
                     break;
                  case 9:
                     gamecanvas3.bossTimer -= 2;
                     gamecanvas3.checkBossContact();
                     j8 = spriteinstance1.y + -40;
                     int i9 = (spriteinstance1.sprite.animFrameStarts[spriteinstance1.anim] + spriteinstance1.animFrame) * 5;
                     byte b8 = spriteinstance1.sprite.animFrames[i9 + 3];
                     if (j8 - b8 >= 240 - gamecanvas3.cameraY + 40) {
                        gamecanvas3.bossAnim = 6;
                        gamecanvas3.bossTimer = 0;
                        int j2 = (j8 = gamecanvas3.playerX - 10) / 3;
                        if (j8 == j2 * 3 + 2) {
                           j2 += stateTicks % 50 / 25;
                        }

                        gamecanvas3.bossPosition = j2;
                     }
                     break;
                  case 10:
                     gamecanvas3.bossTimer--;
                     gamecanvas3.checkBossContact();
                     gamecanvas3.bossSlamTicks++;
                     if (gamecanvas3.bossSlamTicks == 28) {
                        b1 = 7;
                     }

                     if (gamecanvas3.bossSlamTicks >= 50) {
                        gamecanvas3.bossSlamTicks = 0;
                        gamecanvas3.bossAnim = 11;
                        clearObjectPair(gamecanvas3.bossHeldObjectX, gamecanvas3.bossHeldObjectY);
                        gamecanvas3.bossHeldObjectX = -1;
                        gamecanvas3.bossHeldObjectY = -1;
                        b1 = 8;
                        b6 = 2;
                        gamecanvas3.bossSlamVisible = true;
                     }
                     break;
                  case 11:
                     gamecanvas3.bossTimer--;
                     gamecanvas3.bossSlamTicks++;
                     if (gamecanvas3.bossSlamTicks >= 12) {
                        gamecanvas3.bossSlamTicks = 0;
                        gamecanvas3.bossAnim = 9;
                        b1 = 4;
                        gamecanvas3.bossSlamHitPlayer = false;
                        gamecanvas3.bossSlamVisible = false;
                     } else if (!gamecanvas3.bossSlamHitPlayer) {
                        GameCanvas gamecanvas7 = gamecanvas3;
                        j8 = gamecanvas3.bossColumn();
                        if (gamecanvas7.playerY == 4 && gamecanvas7.playerX >= j8 - 3 && gamecanvas7.playerX <= j8 + 4) {
                           gamecanvas7.hurtPlayer(1, 64, gamecanvas7.playerX == j8 ? 4 : 2);
                           gamecanvas7.bossSlamHitPlayer = true;
                        }
                     }
               }

               if (b1 == -1) {
                  spriteinstance1.updateAnim();
               } else {
                  spriteinstance1.setAnim(b1);
               }

               if (gamecanvas3.bossSlamVisible) {
                  if (b6 == -1) {
                     spriteinstance3.updateAnim();
                  } else {
                     spriteinstance3.setAnim(b6);
                  }
               }
               break;
            case 5:
               label1217: {
                  GameCanvas gamecanvas2 = gamecanvas;
                  SpriteInstance spriteinstance = spriteInstances[5];
                  SpriteInstance spriteinstance4;
                  if (gamecanvas2.bossAnim == 12) {
                     if (gamecanvas2.bossTimer++ > 100) {
                        gamecanvas2.bossAnim = 15;
                        gamecanvas2.triggerTile(11, 11);
                     } else {
                        gamecanvas2.sound.play(7);
                     }

                     spriteinstance4 = spriteinstance;
                  } else {
                     if (gamecanvas2.bossAnim == -1) {
                        if (gamecanvas2.playerX * 24 >= 360) {
                           gamecanvas2.bossAnim = 10;
                           spriteinstance.setAnim(10);
                        }
                        break label1217;
                     }

                     if (gamecanvas2.bossAnim == 15 || gamecanvas2.bossAnim == -1) {
                        break label1217;
                     }

                     byte b0 = -1;
                     int k = gamecanvas2.playerX * 24 + 12;
                     int l = gamecanvas2.playerY * 24;
                     int i1 = gamecanvas2.bossX + 24;
                     if (gamecanvas2.bossAnim == 13) {
                        b0 = 13;
                        if (k > i1) {
                           gamecanvas2.knightTurning = true;
                        }
                     }

                     int j1 = gamecanvas2.bossHealth > 0 && stateTicks % gamecanvas2.bossHealth == 0 ? 2 : 1;
                     if (gamecanvas2.bossAnim == 6 || gamecanvas2.bossAnim == 7) {
                        j1 = (stateTicks & 11) == 0 ? 2 : j1;
                     }

                     if (!gamecanvas2.knightCharging && (gamecanvas2.bossAnim == 0 || gamecanvas2.bossAnim == 1)) {
                        GameCanvas gamecanvas8;
                        int i12;
                        if (gamecanvas2.bossAnim == 0) {
                           gamecanvas2.knightChargeOffset = 36;
                           gamecanvas2.knightChargeOffset = ~gamecanvas2.knightChargeOffset;
                           gamecanvas8 = gamecanvas2;
                           i12 = gamecanvas2.knightChargeOffset + 1;
                        } else {
                           gamecanvas8 = gamecanvas2;
                           i12 = 36;
                        }

                        gamecanvas8.knightChargeOffset = i12;
                        if (l < 504 && (k == i1 + gamecanvas2.knightChargeOffset || stateTicks % 76 == 0)) {
                           gamecanvas2.knightCharging = true;
                        }
                     }

                     while (j1-- > 0) {
                        label1197: {
                           switch (gamecanvas2.bossAnim) {
                              case 0:
                                 if (l >= 504 && i1 > 360) {
                                    if (k < i1) {
                                       b0 = 6;
                                       gamecanvas2.bossAnim = 6;
                                    } else {
                                       gamecanvas2.bossX--;
                                    }
                                 } else if (gamecanvas2.knightCharging) {
                                    b0 = 2;
                                    gamecanvas2.bossAnim = 2;
                                 } else if (i1 <= 360) {
                                    b0 = 1;
                                    gamecanvas2.bossAnim = 1;
                                 } else {
                                    gamecanvas2.bossX--;
                                 }
                                 break label1197;
                              case 1:
                                 if (l >= 504 && i1 < 504) {
                                    if (k < i1) {
                                       gamecanvas2.bossX++;
                                    } else {
                                       b0 = 7;
                                       gamecanvas2.bossAnim = 7;
                                    }
                                 } else if (gamecanvas2.knightCharging) {
                                    b0 = 3;
                                    gamecanvas2.bossAnim = 3;
                                 } else if (i1 >= 504) {
                                    b0 = 0;
                                    gamecanvas2.bossAnim = 0;
                                 } else {
                                    gamecanvas2.bossX++;
                                 }
                                 break label1197;
                              case 2:
                                 if (spriteinstance.animFrame == 5 && spriteinstance.frameTimer == 0) {
                                    gamecanvas2.screenShakeTicks = 30;
                                 }

                                 if (spriteinstance.isAnimFinished()) {
                                    b0 = 10;
                                    gamecanvas2.bossAnim = 10;
                                    gamecanvas2.knightCharging = false;
                                    gamecanvas2.knightChargeOffset = 0;
                                 }

                                 if (spriteinstance.animFrame < 7 || l >= 504 || k != i1 + gamecanvas2.knightChargeOffset) {
                                    break label1197;
                                 }
                                 break;
                              case 3:
                                 if (spriteinstance.animFrame == 5 && spriteinstance.frameTimer == 0) {
                                    gamecanvas2.screenShakeTicks = 30;
                                 }

                                 if (spriteinstance.isAnimFinished()) {
                                    b0 = 11;
                                    gamecanvas2.bossAnim = 11;
                                    gamecanvas2.knightCharging = false;
                                    gamecanvas2.knightChargeOffset = 0;
                                 }

                                 if (spriteinstance.animFrame < 7 || l >= 504 || k != i1 + gamecanvas2.knightChargeOffset) {
                                    break label1197;
                                 }
                                 break;
                              case 4:
                              case 5:
                                 if (spriteinstance.isAnimFinished()) {
                                    gamecanvas2.bossAnim = b0 = (byte)(gamecanvas2.bossAnim == 4 ? 0 : 1);
                                 }

                                 gamecanvas2.knightCharging = false;
                                 gamecanvas2.knightChargeOffset = 0;
                                 break label1197;
                              case 6:
                                 if (l >= 504) {
                                    if (k >= i1 - 48) {
                                       b0 = 8;
                                       gamecanvas2.bossAnim = 8;
                                    } else {
                                       gamecanvas2.bossX -= 2;
                                    }
                                 } else if (i1 >= 360) {
                                    b0 = 0;
                                    gamecanvas2.bossAnim = 0;
                                 }
                                 break label1197;
                              case 7:
                                 if (l >= 504) {
                                    if (k <= i1 + 48) {
                                       b0 = 9;
                                       gamecanvas2.bossAnim = 9;
                                    } else {
                                       gamecanvas2.bossX += 2;
                                    }
                                 } else if (i1 <= 504) {
                                    b0 = 1;
                                    gamecanvas2.bossAnim = 1;
                                 }
                                 break label1197;
                              case 8:
                                 if (spriteinstance.isAnimFinished()) {
                                    b0 = 10;
                                    gamecanvas2.bossAnim = 10;
                                 }

                                 if (spriteinstance.animFrame < 4 || l < 504 || k < i1 - 48 || k > i1) {
                                    break label1197;
                                 }
                                 break;
                              case 9:
                                 if (spriteinstance.isAnimFinished()) {
                                    b0 = 11;
                                    gamecanvas2.bossAnim = 11;
                                 }

                                 if (spriteinstance.animFrame < 4 || l < 504 || k < i1 || k > i1 + 48) {
                                    break label1197;
                                 }
                                 break;
                              case 10:
                              case 11:
                                 label1491: {
                                    GameCanvas gamecanvas9;
                                    byte b14;
                                    if (k > i1 && i1 < 504) {
                                       gamecanvas9 = gamecanvas2;
                                       b14 = 1;
                                    } else {
                                       if (k >= i1 || i1 <= 360) {
                                          break label1491;
                                       }

                                       gamecanvas9 = gamecanvas2;
                                       b14 = 0;
                                    }

                                    b0 = b14;
                                    gamecanvas9.bossAnim = b14;
                                 }

                                 gamecanvas2.knightCharging = false;
                                 gamecanvas2.knightChargeOffset = 0;
                              case 12:
                              default:
                                 break label1197;
                              case 13:
                                 if (spriteinstance.isAnimFinished()) {
                                    b0 = 0;
                                    gamecanvas2.bossAnim = 0;
                                    gamecanvas2.knightTurning = false;
                                 } else if (gamecanvas2.knightTurning) {
                                    spriteinstance.updateAnim();
                                 }
                                 break label1197;
                           }

                           gamecanvas2.hurtPlayer(1, 48, 0);
                        }

                        if (l >= 504 && k >= i1 - 24 && k <= i1 - 24) {
                           gamecanvas2.hurtPlayer(1, 48, oppositeDirection[gamecanvas2.playerFlags & 7]);
                        }
                     }

                     if ((gamecanvas2.bossAnim == 8 || gamecanvas2.bossAnim == 9) && spriteinstance.animFrame == 5) {
                        gamecanvas2.restoreArenaWalls();
                     }

                     if (gamecanvas2.bossAnim != 6 && gamecanvas2.bossAnim != 7) {
                        for (int k1 = 21; k1 < 23; k1++) {
                           for (int l1 = i1 / 24 - 1; l1 < i1 / 24 - 2 + 4; l1++) {
                              if (objectMap[l1][k1] == 0) {
                                 label1111:
                                 if ((objectState[l1][k1] & 7) == 3 && gamecanvas2.bossAnim != 13) {
                                    gamecanvas2.bossHealth--;
                                    GameCanvas gamecanvas10;
                                    byte b15;
                                    switch (gamecanvas2.bossAnim) {
                                       case 0:
                                       case 2:
                                       case 4:
                                       case 8:
                                       case 10:
                                          gamecanvas10 = gamecanvas2;
                                          b15 = 4;
                                          break;
                                       case 1:
                                       case 3:
                                       case 5:
                                       case 9:
                                       case 11:
                                          gamecanvas10 = gamecanvas2;
                                          b15 = 5;
                                          break;
                                       case 6:
                                       case 7:
                                       default:
                                          break label1111;
                                    }

                                    b0 = b15;
                                    gamecanvas10.bossAnim = b15;
                                 }

                                 objectMap[l1][k1] = -1;
                                 gamecanvas2.refreshBackgroundTile(l1, k1);
                                 objectMap[l1][k1] = 30;
                                 tileRedrawTimers[l1][k1] = 24;
                                 objectState[l1][k1] = 4;
                                 gamecanvas2.playSound(14);
                              }
                           }
                        }
                     }

                     if (gamecanvas2.bossHealth <= 0) {
                        gamecanvas2.bossAnim = 12;
                        gamecanvas2.bossTimer = 0;
                        b0 = 12;
                     }

                     if (b0 != -1) {
                        spriteinstance.setAnim(b0);
                        break label1217;
                     }

                     spriteinstance4 = spriteinstance;
                  }

                  spriteinstance4.updateAnim();
               }
         }

         if (gameState != 1) {
            return;
         }
      }

      if ((stateTicks & 15) == 0) {
         this.updateCompass();
      }

      if (this.toggleDirection != 0 && (stateTicks >> 1 & 1) == 0) {
         this.togglePhase = this.togglePhase + this.toggleDirection;
         if (this.togglePhase != 0 && this.togglePhase != 9) {
            if (this.togglePhase == 5) {
               int j3 = this.mapHeight - 1;
               int i4 = this.mapWidth - 1;

               for (int i6 = 1; i6 < j3; i6++) {
                  for (int j7 = 1; j7 < i4; j7++) {
                     boolean flag4;
                     label1036: {
                        int l7 = tileMap[j7][i6] & 0xFF;
                        byte b7 = objectMap[j7][i6];
                        flag4 = false;
                        if (l7 == 15) {
                           this.smashObject(j7, i6);
                           objectMap[j7][i6] = 34;
                        } else if (l7 == 16) {
                           this.smashObject(j7, i6);
                           objectMap[j7][i6] = 35;
                        } else {
                           int[] aint1;
                           int j11;
                           byte b24;
                           if (b7 == 34) {
                              aint1 = tileMap[j7];
                              j11 = i6;
                              b24 = 15;
                           } else {
                              if (b7 != 35) {
                                 break label1036;
                              }

                              aint1 = tileMap[j7];
                              j11 = i6;
                              b24 = 16;
                           }

                           aint1[j11] = b24;
                           objectMap[j7][i6] = -1;
                           this.alertCreatures(j7, i6);
                        }

                        flag4 = true;
                     }

                     if (flag4) {
                        markAreaDirty(j7, i6);
                     }
                  }
               }
            }
         } else {
            this.toggleDirection = 0;

            for (int i3 = 1; i3 < this.mapHeight - 1; i3++) {
               for (int l3 = 1; l3 < this.mapWidth - 1; l3++) {
                  int l5 = tileMap[l3][i3] & 0xFF;
                  byte b5 = objectMap[l3][i3];
                  if (l5 == 15 || l5 == 16 || b5 == 34 || b5 == 35) {
                     this.redrawTile(l3, i3);
                  }
               }
            }
         }
      }

      this.invulnerableTicks--;
      if (this.deathTimer > 0 && --this.deathTimer == 0) {
         this.loseLife();
      }

      if (this.moveProgress <= 0 && this.checkpointReached) {
         this.checkpointReached = false;
         this.playSound(9);

         try {
            Thread.sleep(100L);
         } catch (InterruptedException interruptedexception) {
         }

         this.saveCheckpoint();
      }

      if (sprites[11] != null) {
         GameCanvas gamecanvas1 = this;
         int j4;
         GameCanvas gamecanvas13;
         int k11;
         if ((j4 = stateTicks % 89) < 15) {
            gamecanvas13 = gamecanvas1;
            k11 = 0;
         } else if (j4 < 45) {
            gamecanvas13 = gamecanvas1;
            k11 = 48 * (j4 - 15) / 30;
         } else if (j4 < 60) {
            gamecanvas13 = gamecanvas1;
            k11 = 48;
         } else {
            gamecanvas13 = gamecanvas1;
            k11 = 48 - 48 * (j4 - 60) / 30;
         }

         gamecanvas13.trapLengthA = k11;
         gamecanvas1.trapReachA = gamecanvas1.trapLengthA > 0 ? (gamecanvas1.trapLengthA - 1) / 24 + 2 : 1;
         if ((j4 = stateTicks % 44) < 7) {
            gamecanvas13 = gamecanvas1;
            k11 = 0;
         } else if (j4 < 22) {
            gamecanvas13 = gamecanvas1;
            k11 = 48 * (j4 - 7) / 15;
         } else if (j4 < 30) {
            gamecanvas13 = gamecanvas1;
            k11 = 48;
         } else {
            gamecanvas13 = gamecanvas1;
            k11 = 48 - 48 * (j4 - 30) / 15;
         }

         gamecanvas13.trapLengthB = k11;
         gamecanvas1.trapReachB = gamecanvas1.trapLengthB > 0 ? (gamecanvas1.trapLengthB - 1) / 24 + 2 : 1;
      }

      if (this.hookPullSteps != 0 && this.hookX != 0) {
         this.updateHookPull();
      }

      this.updateTilesNearPlayer();
      if (this.collapsingRowCount > 0) {
         this.updateCollapsingRows();
      }

      this.updateEnemies();
      if (this.switchRevealPhase != 0) {
         this.updateSwitchReveal();
      }

      if (this.activeCutscene != null) {
         this.idleAnimDelay = 70;
         if (this.activeCutscene.isStepFinished() && this.activeCutscene.nextStep() == null) {
            this.activeCutscene = null;
         }

         if (this.activeCutscene != null) {
            this.activeCutscene.update();
         }
      } else if (this.pendingHint != -1) {
         this.startCutscene(this.pendingHint);
         this.pendingHint = -1;
         this.playerAction = 0;
      }

      label1011: {
         GameCanvas gamecanvas14;
         int b20;
         if (this.escapePressesLeft > 0) {
            if (objectMap[this.playerX][this.playerY] < 0) {
               byte b23;
               if (objectMap[this.playerX][this.playerY + 1] == 9 && (objectState[this.playerX][this.playerY + 1] & 7) == 3) {
                  this.playerY++;
                  gamecanvas14 = this;
                  b20 = this.playerFlags & -8;
                  b23 = 3;
               } else if (objectMap[this.playerX - 1][this.playerY + 1] == 9 && (objectState[this.playerX - 1][this.playerY + 1] & 7) == 3) {
                  this.playerY++;
                  this.playerX--;
                  gamecanvas14 = this;
                  b20 = this.playerFlags & -8;
                  b23 = 3;
               } else if (objectMap[this.playerX + 1][this.playerY + 1] == 9 && (objectState[this.playerX + 1][this.playerY + 1] & 7) == 3) {
                  this.playerY++;
                  this.playerX++;
                  gamecanvas14 = this;
                  b20 = this.playerFlags & -8;
                  b23 = 3;
               } else {
                  gamecanvas14 = this;
                  b20 = this.playerFlags & -8;
                  b23 = 0;
               }

               gamecanvas14.playerFlags = b20 | b23;
            }

            gamecanvas14 = this;
            b20 = objectTimers[this.playerX][this.playerY];
         } else {
            if ((this.queuedAction == 0 || this.moveProgress <= 0 && this.queuedAction != 5)
               && !this.cheatSkipWalking
               && (this.playerFlags & 112) == 0
               && this.pickupIconSet == -1) {
               this.queuedAction = this.playerAction;
               boolean flag1 = false;
               if (this.entryWalkSteps > 0) {
                  this.queuedAction = 2;
                  this.entryWalkSteps--;
                  if (this.entryWalkSteps == 0) {
                     this.closeGateAt(this.playerX - 1, this.playerY);
                     this.playerAction = 0;
                     keysDown = 0;
                  }
               } else if (flag) {
                  markAreaDirty(this.playerX, this.playerY);
               }

               label735:
               switch (this.queuedAction) {
                  case 0:
                     this.playerFlags &= -9;
                     boolean flag3 = false;
                     switch (spriteInstances[0].anim) {
                        default:
                           if ((this.playerFlags & 16384) == 0) {
                              flag3 = true;
                           }
                        case 0:
                        case 2:
                        case 10:
                        case 11:
                        case 12:
                        case 34:
                        case 35:
                        case 40:
                        case 48:
                           if (flag3) {
                              int i8;
                              if ((i8 = 0 + (this.playerFlags & 7) - 1) < 0) {
                                 i8 = 0;
                              }

                              this.setPlayerAnim(i8);
                           }

                           byte b22;
                           if (this.activeCutscene == null) {
                              gamecanvas14 = this;
                              b22 = 6;
                           } else {
                              gamecanvas14 = this;
                              b22 = 0;
                           }

                           gamecanvas14.moveSpeed = b22;
                           if (this.moveProgress > 0) {
                              this.moveProgress -= 6;
                           }
                           break label735;
                     }
                  case 3:
                     this.playerFlags &= -9;
                  case 1:
                  case 2:
                  case 4:
                     if (spriteInstances[0].anim != 40 && spriteInstances[0].anim != 48) {
                        if ((this.playerFlags & 4096) == 0) {
                           flag1 = this.tryMovePlayer(-directionOffsets[this.queuedAction], -directionOffsets[this.queuedAction + 8], false);
                           flag = this.isPlayerInsideBorder();
                           if (flag1) {
                              this.crushTimer = 40;
                              this.underHeavyObject = false;
                              int l4;
                              if ((l4 = this.queuedAction - 1) < 0) {
                                 l4 = 0;
                              }

                              if ((this.playerFlags & 8) != 0) {
                                 if (this.queuedAction == 2) {
                                    this.setPlayerAnim(8);
                                 } else {
                                    this.setPlayerAnim(9);
                                 }
                              } else {
                                 this.setPlayerAnim(l4 + 4);
                              }
                           }
                        } else {
                           this.playerFlags = this.playerFlags & -8 | this.queuedAction;
                           this.moveProgress = 18;
                           int i5;
                           if ((i5 = this.queuedAction - 1) < 0) {
                              i5 = 0;
                           }

                           this.setPlayerAnim(i5 + 0);
                        }
                     }
                     break;
                  case 5:
                     this.moveProgress = 0;
                     int j6 = this.playerFlags & 7;
                     if (this.isPlayerInsideBorder()) {
                        this.setPlayerAnim(j6 + 13 - 1);
                     }
                     break;
                  case 6:
                     int k4 = (this.playerFlags & 7) == 2 ? 1 : -1;
                     if (flag && objectMap[this.playerX + k4][this.playerY] < 0) {
                        this.setPlayerAnim(k4 == -1 ? 22 : 20);
                        objectMap[this.playerX + k4][this.playerY] = 32;
                        objectTimers[this.playerX + k4][this.playerY] = 18;
                        objectState[this.playerX + k4][this.playerY] = 4 | (k4 > 0 ? 1 : 0);
                        tileRedrawTimers[this.playerX + k4][this.playerY] = 30;
                        this.moveProgress = 72;
                        this.playerAction = 0;
                     }
               }

               if (flag) {
                  byte b3 = objectMap[this.playerX][this.playerY - 1];
                  if (!flag1
                     && this.queuedAction != 5
                     && this.moveProgress <= 0
                     && (b3 == 0 || b3 == 9 || b3 == 8 || b3 == 48)
                     && (tileMap[this.playerX][this.playerY] & 0xFF) != 35) {
                     int k6 = 0;
                     if (tileSlots != null) {
                        k6 = slotBits(tileSlots[this.playerX][this.playerY], (byte)0, (byte)3, (byte)4);
                     }

                     if (!this.hasEnemies || k6 == 0 && k6 != 3) {
                        if ((this.playerFlags & 8) == 0 && spriteInstances[0].anim != 11 && spriteInstances[0].anim != 10 && spriteInstances[0].anim != 12) {
                           this.setPlayerAnim(11);
                        }

                        this.underHeavyObject = true;
                        if (this.crushTimer <= 0) {
                           this.crushTimer = 40;
                           this.invulnerableTicks = 0L;
                           this.hurtPlayer(saveData[8], 32, 0);
                           return;
                        }
                     }
                  }
               }

               if (flag1) {
                  this.stuckWarning = false;
               } else if (flag && (stateTicks & 31) == 0) {
                  boolean flag2 = objectMap[this.playerX][this.playerY - 1] == 0;

                  for (int l6 = 1; !flag2 && l6 <= 4; l6++) {
                     flag2 |= this.tryMovePlayer(directionOffsets[l6], directionOffsets[l6 + 8], true);
                  }

                  if (!flag2) {
                     if (this.stuckWarning) {
                        this.setPlayerAnim(19);
                     } else {
                        this.stuckWarning = true;
                     }
                  }
               }
               break label1011;
            }

            if (this.cheatSkipWalking && this.moveProgress <= 0) {
               byte b21;
               if (this.cheatSkipExitA) {
                  gamecanvas14 = this;
                  b21 = this.exitDirectionA;
               } else {
                  gamecanvas14 = this;
                  b21 = this.exitDirectionB;
               }

               gamecanvas14.queuedAction = b21;
               this.playerFlags = this.playerFlags & -8 | this.queuedAction;
               this.playerX = this.playerX - directionOffsets[this.queuedAction];
               this.playerY = this.playerY - directionOffsets[this.queuedAction + 8];
               flag = this.isPlayerInsideBorder();
               this.moveProgress = 18;
               if (spriteInstances[0].anim != 4) {
                  this.setPlayerAnim(4 + this.queuedAction - 1);
               }
            }

            if ((this.playerFlags & 112) > 32 && (this.playerFlags & 2048) == 0) {
               break label1011;
            }

            this.moveProgress -= 6;
            if (this.moveProgress > 0) {
               break label1011;
            }

            this.playerFlags &= -4209;
            gamecanvas14 = this;
            b20 = 0;
         }

         gamecanvas14.moveProgress = b20;
      }

      if (this.switchRevealPhase == 0) {
         if (this.cameraPanToCheckpoint) {
            this.cameraPanSpeed = 8;
            if (this.panCameraToTarget()) {
               this.addEffect(this.checkpointX, this.checkpointY, 5);
               this.cameraPanToCheckpoint = false;
            }
         } else if (this.activeCutscene == null) {
            if (cameraHeldAfterReveal && this.queuedAction != 0) {
               cameraHeldAfterReveal = false;
               this.idleAnimDelay = 0;
            }

            if (!cameraHeldAfterReveal) {
               this.updateCamera();
            }
         } else {
            this.cameraX = this.cameraTargetX;
            this.cameraY = this.cameraTargetY;
         }
      }

      if (this.queuedAction != 0 && flag) {
         this.onSolidGround = objectMap[this.playerX][this.playerY + 1] >= 0;
      }

      this.updatePlayer();
      if (this.cheatSkipWalking && (this.playerX < -5 || this.playerX > this.mapWidth + 5 || this.playerY < -5 || this.playerY > this.mapHeight + 5)) {
         this.unloadCutscenes();
         if (!this.cheatSkipExitA && this.stage < this.firstSecretStage) {
            gameState = 20;
            this.loadingStep = 0;
            this.commitTakenTreasures();
            this.unlockNextStages();
            addToSaveShort(6, this.stageRedDiamonds);
         } else {
            this.loadingStep = 0;
            this.loadingStepCount = 12;
            gameState = 35;
         }

         this.saveProgress(false);
         this.writeSave();
         this.resetStateTimer();
         this.subState = -1;
      }
   }

   private static byte scanFallRun(int var0, int var1, int var2, int var3) {
      int i = var0 + var1;
      if ((var0 > 0 && var2 == 0 || var0 < var2 && var2 > 0) && (objectMap[i][var3] < 0 || objectMap[i][var3] == 31 || fallingVariant(i, var3) >= 0)) {
         do {
            var0 = i;
            i += var1;
         } while ((var0 > 0 && var2 == 0 || var0 < var2 && var2 > 0) && (objectMap[i][var3] < 0 || fallingVariant(i, var3) >= 0 || objectMap[i][var3] == 31));
      }

      return (byte)var0;
   }

   private void updateCollapsingRows() {
      int i = this.mapWidth - 1;

      for (int b0 = 0; b0 < collapsingRows.length; b0 += 3) {
         byte b1;
         if ((b1 = collapsingRows[b0 + 2]) > 0) {
            byte b2 = collapsingRows[b0 + 1];
            byte b3 = collapsingRows[b0];
            b3 = collapsingRows[b0] = scanFallRun(b3, -1, 0, b1);
            b2 = collapsingRows[b0 + 1] = scanFallRun(b2, 1, i, b1);

            for (int j = b3; j <= b2; j++) {
               this.collapseTile(j, b1);
            }
         }
      }
   }

   private boolean collapseTile(int var1, int var2) {
      int i = fallingVariant(var1, var2);
      int j = 0;
      if (i >= 0) {
         if (i == 37 && objectMap[var1][var2] == 43) {
            j = 268435456;
         }

         markAreaDirty(var1, var2);
         objectMap[var1][var2] = 9;
         objectState[var1][var2] = objectState[var1][var2] & -264241153 | i << 22 | j;
         this.addEffect(var1, var2, 1);
      }

      j = (this.playerFlags & 16384) == 0 ? 0 : 3;
      SpriteInstance spriteinstance = spriteInstances[j];
      if (this.isPlayerAt(var1, var2) && this.escapePressesLeft <= 0 && spriteinstance.anim != 40 && spriteinstance.anim != 48 && spriteinstance.anim != 47) {
         this.hurtPlayer(0, 16, 0);
      }

      return i >= 0;
   }

   private static int fallingVariant(int var0, int var1) {
      if (objectMap[var0][var1] < 0) {
         return -1;
      }

      switch (objectMap[var0][var1]) {
         case 1:
            return 34;
         case 19:
         case 43:
            return 37;
         case 45:
            return 35;
         case 46:
            return 36;
         case 49:
            return 39;
         default:
            return -1;
      }
   }

   public static int readShort(byte[] var0, int var1) {
      return var0[var1] & 0xFF | (var0[var1 + 1] & 0xFF) << 8;
   }

   public static int readInt(byte[] var0, int var1) {
      return var0[var1] & 0xFF | (var0[var1 + 1] & 0xFF) << 8 | (var0[var1 + 2] & 0xFF) << 16 | (var0[var1 + 3] & 0xFF) << 24;
   }

   private static int addToSaveShort(int var0, int var1) {
      if ((var1 = readShort(saveData, var0) + var1) < 0) {
         var1 = 0;
      }

      saveData[var0] = (byte)var1;
      saveData[var0 + 1] = (byte)(var1 >> 8);
      return var1;
   }

   private void commitTakenTreasures() {
      for (int i = 0; i < this.takenTreasureCount; i++) {
         this.markTreasureTaken(this.world, this.stage, takenTreasures[i << 1] & 255, takenTreasures[(i << 1) + 1] & 255);
      }
   }

   private int bankStageDiamonds() {
      return addToSaveShort(4, this.stageDiamonds);
   }

   private void addEffect(int var1, int var2, int var3) {
      int i = this.nextEffect << 1;
      effectPositions[i] = (byte)var1;
      effectPositions[i + 1] = (byte)var2;
      effectTypes[this.nextEffect] = (byte)var3;
      effectTimers[this.nextEffect] = 0;
      this.nextEffect = this.nextEffect + 1 & 7;
   }

   private void loseLife() {
      this.lives--;
      this.stageRetries++;
      if (this.lives >= 0) {
         this.restoreCheckpoint();
         this.energy = saveData[8];
         this.hudDirty = true;
         this.playerFlags = 0;
         this.escapePressesLeft = 0;
         this.playerAction = this.queuedAction = 0;
      } else {
         gameState = 12;
      }
   }

   private void activateGate(int var1) {
      if (var1 >= 0) {
         byte b0 = doorX[var1];
         byte b1 = doorY[var1];
         int i = tileMap[b0][b1] & 0xFF;
         int j;
         if (((j = tileMap[b0][b1] >> 8) & 240) == 0) {
            if (i != 7 || (j >> 8 & 0xFF) != var1) {
               System.out.println("!!!!!!!!!!!!!! door missing");
            }

            var1 = j & 15;
            if (--var1 == 0) {
               j = j & -241 | 16;
               this.propagateTileFlag(b0, b1 - 1, 1, 0, 1);
               this.propagateTileFlag(b0, b1 - 1, -1, 0, 1);
               var1 = tileMap[b0][b1 - 1] >> 8 | 512;
               tileMap[b0][b1 - 1] = var1 << 8 | tileMap[b0][b1 - 1] & 0xFF;
               tileRedrawTimers[b0][b1] = 24;
            } else {
               j = j & -16 | var1;
            }

            tileMap[b0][b1] = j << 8 | i;
            this.playSound(8);
         }
      }
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   private void closeGateAt(int var1, int var2) {
      int i;
      try {
         i = tileMap[var1][var2] >> 8;
         if ((tileMap[var1][var2] & 0xFF) != 7) {
            return;
         }
      } catch (Exception exception2) {
         return;
      }

      if ((i & 240) != 0) {
         try {
            if (objectMap[var1][var2] == 32) {
               return;
            }
         } catch (Exception exception1) {
            return;
         }

         try {
            this.playSound(14);
            i &= -241;
            tileMap[var1][var2] = i << 8 | 7;
            this.propagateTileFlag(var1, var2 - 1, 1, 0, 0);
            this.propagateTileFlag(var1, var2 - 1, -1, 0, 0);
            if (this.isPlayerAt(var1, var2)) {
               this.invulnerableTicks = 0L;
               this.hookPullSteps = 0;
               this.hurtPlayer(saveData[8], 48, 0);
               this.playSound(2);
            } else {
               switch (objectMap[var1][var2]) {
                  case 0:
                  case 1:
                  case 19:
                  case 43:
                  case 45:
                     objectMap[var1][var2] = -1;
                     this.refreshBackgroundTile(var1, var2);
                     this.triggerTile(var1, var2);
               }
            }

            tileRedrawTimers[var1][var2] = 24;
         } catch (Exception exception) {
         }
      }
   }

   private void closeGateById(int var1) {
      if (var1 >= 0) {
         try {
            this.closeGateAt(doorX[var1], doorY[var1]);
         } catch (Exception exception) {
         }
      }
   }

   private void updatePlayer() {
      int i = (this.playerFlags & 16384) == 0 ? 0 : 3;
      SpriteInstance spriteinstance = spriteInstances[i];
      int j = -1;
      boolean flag = true;
      boolean flag1 = false;
      boolean flag2;
      if (flag2 = spriteinstance.isAnimFinished()) {
         this.playerFlags &= -8193;
      }

      label442: {
         int l3;
         if (i == 3) {
            if (!flag2) {
               break label442;
            }

            this.playerFlags &= -18433;
            l3 = this.hitRecoveryAnim();
         } else {
            label432: {
               byte b3;
               switch (spriteinstance.anim) {
                  case 0:
                     flag1 = true;
                     l3 = 0;
                     break label432;
                  case 1:
                  case 3:
                     if (flag2) {
                        j = 0 + (this.playerFlags & 7) - 1;
                     }

                     flag1 = true;
                     break label442;
                  case 2:
                     flag1 = true;
                     l3 = 2;
                     break label432;
                  case 4:
                  case 6:
                     if (!this.onSolidGround) {
                        break label442;
                     }

                     l3 = spriteinstance.anim;
                     break label432;
                  case 5:
                     if (this.onSolidGround) {
                        break label442;
                     }

                     l3 = 24;
                     break label432;
                  case 7:
                     if (this.onSolidGround) {
                        break label442;
                     }

                     l3 = 25;
                     break label432;
                  case 8:
                     if (this.onSolidGround) {
                        break label442;
                     }

                     l3 = 26;
                     break label432;
                  case 9:
                     if (this.onSolidGround) {
                        break label442;
                     }

                     l3 = 27;
                     break label432;
                  case 10:
                     if (!flag2) {
                        this.invulnerableTicks = 40L;
                        break label442;
                     }

                     this.playerFlags &= -2049;
                     l3 = this.hitRecoveryAnim();
                     break label432;
                  case 11:
                     if (objectMap[this.playerX][this.playerY - 1] != -1) {
                        if (flag2 && objectMap[this.playerX][this.playerY - 1] != -1) {
                           this.invulnerableTicks = 0L;
                           this.hurtPlayer(saveData[8], 32, 0);
                        }
                        break label442;
                     }

                     b3 = 0;
                     break;
                  case 12:
                     if (this.fadeHeight < 120) {
                        this.fadeHeight += 12;
                     }
                     break label442;
                  case 13:
                  case 14:
                  case 15:
                  case 16:
                  case 28:
                  case 29:
                     if (!flag2) {
                        if ((this.playerFlags & 8192) == 0 && spriteinstance.animFrame == 2 && spriteinstance.frameTimer == 0) {
                           i = this.playerX - directionOffsets[this.playerFlags & 7];
                           int i2 = this.playerY - directionOffsets[(this.playerFlags & 7) + 8];
                           if (saveData[9] >= 8 && spriteinstance.animFrame == 2 && spriteinstance.frameTimer == 0) {
                              this.addEffect(i, i2, -1);
                           }

                           int[] aint;
                           int flag4;               // 0/1 "hit something", then object types
                           int tx;                  // x of the tile the weapon hits
                           GameCanvas gamecanvas2;
                           boolean flag5;
                           boolean flag6;
                           int[] aint1;
                           label372: {
                              i2 = i2;
                              tx = i;
                              gamecanvas2 = this;
                              aint1 = new int[]{0, 1, -1, 0, 0};
                              aint = new int[]{0, 0, 0, 1, -1};
                              flag4 = 0;
                              flag5 = saveData[9] >= 8;
                              flag6 = false;
                              boolean flag11;
                              switch (objectMap[tx][i2]) {
                                 case 0:
                                    flag6 = true;
                                    break label372;
                                 case 9:
                                    if ((gamecanvas2.playerFlags & 8192) != 0) {
                                       break label372;
                                    }

                                    GameCanvas gamecanvas;
                                    int l;
                                    int i1;
                                    label368: {
                                       gamecanvas2.playerFlags |= 8192;
                                       flag4 = 1;
                                       i1 = i2;
                                       l = tx;
                                       gamecanvas = gamecanvas2;
                                       int k1 = i1;
                                       int j1 = l;
                                       if (collapsingRows != null) {
                                          for (int b0 = 0; b0 < collapsingRows.length; b0 += 3) {
                                             if (collapsingRows[b0 + 2] == k1 && (collapsingRows[b0] - 1 == j1 || collapsingRows[b0 + 1] + 1 == j1)) {
                                                flag11 = true;
                                                break label368;
                                             }
                                          }
                                       }

                                       flag11 = false;
                                    }

                                    if (!flag11) {
                                       markAreaDirty(l, i1);
                                       int i3 = gamecanvas.isPlayerAt(l, i1 - 1) ? 2 : 1;
                                       switch ((objectState[l][i1] & 264241152) >> 22) {
                                          case 34:
                                             objectMap[l][i1] = 1;
                                             gamecanvas.redrawTile(l, i1);
                                             break label372;
                                          case 35:
                                             objectMap[l][i1] = 45;
                                             objectState[l][i1] = objectState[l][i1] & -16 | 10;
                                             break label372;
                                          case 36:
                                             objectMap[l][i1] = 46;
                                             objectState[l][i1] = 0;
                                             objectTimers[l][i1] = 0;
                                             break label372;
                                          case 37:
                                             byte[] abyte;
                                             int i4;
                                             byte b5;
                                             if ((objectState[l][i1] & 268435456) != 0) {
                                                abyte = objectMap[l];
                                                i4 = i1;
                                                b5 = 43;
                                             } else {
                                                abyte = objectMap[l];
                                                i4 = i1;
                                                b5 = 19;
                                             }

                                             abyte[i4] = b5;
                                             objectState[l][i1] = i3;
                                             gamecanvas.hitObject(19, l, i1);
                                          case 38:
                                          default:
                                             break label372;
                                          case 39:
                                             objectMap[l][i1] = 49;
                                             objectState[l][i1] = i3;
                                       }
                                    }
                                    break label372;
                                 case 10:
                                    if (gamecanvas2.enemyPhase == 3 && objectState[tx][i2] <= 0) {
                                       flag4 = 1;
                                       objectState[tx][i2] = 1;
                                       gamecanvas2.refreshBackgroundTile(tx, i2);
                                    }
                                    break label372;
                                 case 16:
                                    flag4 = 1;
                                    flag6 = true;
                                    break label372;
                                 case 18:
                                    flag11 = gamecanvas2.tryStartFloorToggle();
                                    break;
                                 case 30:
                                    flag4 = 1;
                                    if (objectState[tx][i2] == 0) {
                                       gamecanvas2.playSound(11);
                                       objectState[tx][i2] = 1;
                                    }
                                    break label372;
                                 default:
                                    if (objectMap[tx][i2] - 80 < 0 && ((tileMap[tx][i2] & 0xFF) != 7 || (tileMap[tx][i2] >> 8 & 240) != 0)) {
                                       break label372;
                                    }

                                    flag6 = true;
                                    flag11 = true;
                              }

                              flag4 = flag11 ? 1 : 0;
                           }

                           if (flag6) {
                              vibrate(200);
                              gamecanvas2.playSound(6);
                              gamecanvas2.setPlayerAnim(41 + (gamecanvas2.playerFlags & 7) - 1);
                           }

                           if (flag4 == 0) {
                              for (int j2 = 0; j2 < 5; j2++) {
                                 int k2 = tx + aint1[j2];
                                 int l2 = i2 + aint[j2];
                                 if (k2 >= 0 && k2 < gamecanvas2.mapWidth && l2 >= 0 && l2 < gamecanvas2.mapHeight) {
                                    flag4 = objectMap[k2][l2];
                                    boolean flag9 = false;
                                    boolean flag10 = false;
                                    switch (flag4) {
                                       case 1:
                                          if (k2 == gamecanvas2.playerX - directionOffsets[gamecanvas2.playerFlags & 7]
                                             && l2 == gamecanvas2.playerY - directionOffsets[(gamecanvas2.playerFlags & 7) + 8]) {
                                             flag10 = true;
                                          }
                                          break;
                                       case 19:
                                       case 43:
                                       case 45:
                                       case 46:
                                       case 49:
                                          int j3;
                                          byte b2 = (j3 = objectState[k2][l2] & 7) == 0 ? 0 : objectTimers[k2][l2];
                                          boolean flag7 = flag4 != 49 && flag4 != 46;
                                          if (actorsTouch(tx, i2, 0, 0, k2, l2, j3, b2)) {
                                             flag10 = true;
                                             flag9 = flag7;
                                          }

                                          if (flag10) {
                                             gamecanvas2.playSound(10);
                                          }
                                    }

                                    if (flag5 && flag10 && (k2 != gamecanvas2.playerX || l2 != gamecanvas2.playerY)) {
                                       if (gamecanvas2.collapseTile(k2, l2)) {
                                          gamecanvas2.playerFlags |= 8192;
                                       }
                                    } else if (flag9) {
                                       gamecanvas2.playerFlags |= 8192;
                                       if (flag4 == 45) {
                                          int k3;
                                          if ((k3 = (objectState[k2][l2] & 7168) >> 10) == 3) {
                                             objectMap[k2][l2] = -1;
                                             gamecanvas2.triggerTile(k2, l2);
                                          } else {
                                             objectState[k2][l2] = 10 | ++k3 << 10;
                                             objectState[k2][l2] = objectState[k2][l2] & -249 | 120;
                                             objectTimers[k2][l2] = 0;
                                          }
                                       } else {
                                          gamecanvas2.hitObject(flag4, k2, l2);
                                       }
                                    }
                                 }
                              }
                           }

                           this.playerAction = 0;
                           keysDown = 0;
                        }
                        break label442;
                     }

                     keysDown = 0;
                     this.queuedAction = 0;
                     this.moveProgress = 0;
                     b3 = 0;
                     break;
                  case 17:
                  case 18:
                     if (flag2) {
                        j = 0 + (this.playerFlags & 7) - 1;
                        this.moveProgress = 0;
                     } else if (spriteinstance.animFrame > 0 && this.pickupIconSet != -1) {
                        i = (this.playerFlags & 7) == 2 ? this.playerX + 1 : this.playerX - 1;
                        int l1 = tileMap[i][this.playerY] >> 8;
                        int lockType = tileMap[i][this.playerY] & 0xFF;
                        l1 |= 256;
                        if (lockType == 9) {
                           this.goldKeys--;
                        } else {
                           this.silverKeys--;
                        }

                        tileMap[i][this.playerY] = l1 << 8 | lockType;
                        this.activateGate(l1 & 0xFF);
                        this.pickupIconSet = this.pickupValue = -1;
                     }
                     break label442;
                  case 19:
                     if (flag2) {
                        flag = false;
                        this.loseLife();
                     }
                     break label442;
                  case 20:
                  case 21:
                  case 22:
                  case 23:
                  case 30:
                  case 31:
                  case 32:
                  case 33:
                     if (flag2) {
                        flag = false;
                        keysDown = 0;
                     }
                     break label442;
                  case 24:
                     if (!this.onSolidGround) {
                        break label442;
                     }

                     l3 = 5;
                     break label432;
                  case 25:
                     if (!this.onSolidGround) {
                        break label442;
                     }

                     l3 = 7;
                     break label432;
                  case 26:
                     if (this.onSolidGround) {
                        break label442;
                     }

                     l3 = 8;
                     break label432;
                  case 27:
                     if (this.onSolidGround) {
                        break label442;
                     }

                     l3 = 9;
                     break label432;
                  case 34:
                     flag1 = true;
                     l3 = 34;
                     break label432;
                  case 35:
                     flag1 = true;
                     l3 = 35;
                     break label432;
                  case 36:
                  case 37:
                  case 38:
                  case 39:
                     b3 = 4;
                     break;
                  case 40:
                  case 48:
                     this.moveProgress = 0;
                     this.playerAction = this.queuedAction = 0;
                     if (spriteinstance.frameTimer == 0) {
                        if (spriteinstance.animFrame == (spriteinstance.anim == 40 ? 12 : 6)) {
                           this.playSound(4);
                        }

                        if (spriteinstance.animFrame == (spriteinstance.anim == 40 ? 13 : 6)) {
                           GameCanvas gamecanvas1 = this;
                           boolean flag8 = false;
                           byte b1 = -1;
                           boolean flag3 = false;
                           switch (gamecanvas1.pickupObject) {
                              case 2:
                                 gamecanvas1.stageRedDiamonds++;
                                 flag3 = true;
                                 b1 = 0;
                              case 3:
                              case 8:
                              case 9:
                              case 10:
                              case 11:
                              case 12:
                              case 13:
                              case 14:
                              case 15:
                              case 16:
                              case 17:
                              case 18:
                              case 19:
                              case 20:
                              case 21:
                              case 22:
                              case 23:
                              case 25:
                              case 28:
                              case 29:
                              case 30:
                              case 31:
                              case 32:
                              case 33:
                              case 34:
                              case 35:
                              case 36:
                              case 37:
                              case 38:
                              case 39:
                              case 43:
                              case 44:
                              case 45:
                              case 46:
                              case 47:
                              case 48:
                              case 49:
                              case 50:
                              default:
                                 break;
                              case 4:
                                 b1 = 2;
                                 gamecanvas1.goldKeys++;
                                 break;
                              case 5:
                                 gamecanvas1.silverKeys++;
                                 b1 = 1;
                                 break;
                              case 6:
                                 gamecanvas1.lives++;
                                 checkpointObjectMap[gamecanvas1.playerX][gamecanvas1.playerY] = -1;
                                 checkpointTileMap[gamecanvas1.playerX][gamecanvas1.playerY] = checkpointTileMap[gamecanvas1.playerX][gamecanvas1.playerY]
                                    | 256;
                                 b1 = 0;
                                 gamecanvas1.markTreasureTaken(gamecanvas1.world, gamecanvas1.stage, gamecanvas1.playerX, gamecanvas1.playerY);
                                 break;
                              case 7:
                                 gamecanvas1.setEnergy((byte)127);
                                 b1 = 4;
                                 break;
                              case 24:
                                 flag3 = true;
                                 flag8 = true;
                                 gamecanvas1.pendingHint = 22;
                                 break;
                              case 26:
                                 flag8 = true;
                                 gamecanvas1.pendingHint = 25;
                                 flag3 = true;
                                 break;
                              case 27:
                                 flag3 = true;
                                 flag8 = true;
                                 gamecanvas1.pendingHint = 23;
                                 break;
                              case 40:
                                 flag3 = true;
                                 flag8 = true;
                                 gamecanvas1.pendingHint = 24;
                                 break;
                              case 41:
                                 if (gamecanvas1.isBossStage()) {
                                    gamecanvas1.bossExitPending = true;
                                    gamecanvas1.resetStateTimer();
                                 }

                                 gamecanvas1.stageDiamonds = gamecanvas1.stageDiamonds + gamecanvas1.pickupValue;
                                 gamecanvas1.padlockGemsRemaining = gamecanvas1.padlockGemsRemaining - gamecanvas1.pickupValue;
                                 if (gamecanvas1.padlockGemsRemaining <= 0 && !gamecanvas1.isBossStage()) {
                                    objectMap[gamecanvas1.padlockX][gamecanvas1.padlockY] = -1;
                                    gamecanvas1.padlockGemsRemaining = 0;
                                 }

                                 b1 = 3;
                                 break;
                              case 42:
                                 flag3 = true;
                                 flag8 = true;
                                 gamecanvas1.hasCompass = true;
                                 gamecanvas1.pendingHint = 11;
                                 break;
                              case 51:
                              case 52:
                              case 53:
                                 gamecanvas1.markTreasureTaken(gamecanvas1.world, gamecanvas1.stage, gamecanvas1.playerX, gamecanvas1.playerY);
                                 b1 = 0;
                                 flag8 = true;
                                 gamecanvas1.resetStateTimer();
                                 gamecanvas1.crystalExitPending = true;
                                 GameCanvas gamecanvas3;
                                 byte b4;
                                 if (gamecanvas1.pickupObject == 53) {
                                    gamecanvas3 = gamecanvas1;
                                    b4 = 0;
                                 } else if (gamecanvas1.pickupObject == 51) {
                                    gamecanvas3 = gamecanvas1;
                                    b4 = 1;
                                 } else {
                                    gamecanvas3 = gamecanvas1;
                                    b4 = 2;
                                 }

                                 gamecanvas3.flyingCrystal = b4;
                                 saveData[2] = (byte)(saveData[2] | 1 << gamecanvas1.flyingCrystal);
                                 gamecanvas1.writeSave();
                           }

                           if (flag8) {
                              gamecanvas1.setPlayerAnim(47);
                           }

                           if (b1 != -1) {
                              gamecanvas1.addEffect(gamecanvas1.playerX, gamecanvas1.playerY - 1, b1);
                           }

                           if (flag3) {
                              takenTreasures[gamecanvas1.takenTreasureCount << 1] = (byte)gamecanvas1.playerX;
                              takenTreasures[(gamecanvas1.takenTreasureCount << 1) + 1] = (byte)gamecanvas1.playerY;
                              gamecanvas1.takenTreasureCount++;
                           }

                           gamecanvas1.hudDirty = true;
                           gamecanvas1.pickupObject = -1;
                        }
                     }

                     if (spriteinstance.isAnimFinished()) {
                        this.lastPickupTime = System.currentTimeMillis();
                        j = 0 + (this.playerFlags & 7) - 1;
                        this.pickupIconSet = this.pickupIconFrame = this.pickupValue = -1;
                     }
                     break label442;
                  case 41:
                  case 42:
                  case 43:
                  case 44:
                  case 45:
                  case 46:
                     if (flag2) {
                        j = 0 + (this.playerFlags & 7) - 1;
                        this.queuedAction = this.playerAction = 0;
                        this.moveProgress = 0;
                        keysDown = 0;
                     }
                     break label442;
                  case 47:
                     this.moveProgress = 0;
                     this.playerAction = this.queuedAction = 0;
                     if ((stateTicks & 1) == 0) {
                        i = this.playerX - 2 + stateTicks % 5;
                        int k = this.playerY - 2 + stateTicks % 3;
                        if (i == this.playerX && (k == this.playerY || k == this.playerY - 1)) {
                           i += (stateTicks >> 1 & 1) == 0 ? 1 : -1;
                        }

                        this.addEffect(i, k, stateTicks * 3 % 5);
                     }

                     if (flag2) {
                        j = 0 + (this.playerFlags & 7) - 1;
                        this.pickupIconSet = this.pickupIconFrame = this.pickupValue = -1;
                     }
                     break label442;
                  default:
                     b3 = 36;
               }

               l3 = b3 + (this.playerFlags & 7) - 1;
            }
         }

         j = l3;
      }

      GameCanvas gamecanvas4;
      int j4;
      if (flag1) {
         gamecanvas4 = this;
         j4 = this.idleAnimDelay - 1;
      } else {
         gamecanvas4 = this;
         j4 = 70;
      }

      gamecanvas4.idleAnimDelay = j4;
      if (j != -1) {
         this.setPlayerAnim(j);
      }

      if (flag) {
         spriteinstance.updateAnim();
      }

      this.turnCooldown = this.turnCooldown > 0 ? --this.turnCooldown : 0;
   }

   private int hitRecoveryAnim() {
      int i;
      if (this.energy <= 0) {
         this.playSound(2);
         i = 12;
         this.deathTimer = 80;
      } else {
         i = 0 + (this.playerFlags & 7) - 1;
         this.playerFlags &= -113;
         this.invulnerableTicks = 40L;
      }

      this.queuedAction = 0;
      return i;
   }

   private static void vibrate(int var0) {
      if (vibrationEnabled) {
         midlet.display.vibrate(200);
      }
   }

   private void updateHookPull() {
      if (this.hookPullDelay <= 0) {
         int i = this.hookPullSteps > 0 ? 1 : -1;
         this.hookPullSteps -= i;
         if (this.hookPullSteps != 0) {
            if (objectMap[this.hookX][this.hookY] == 48) {
               int j = objectState[this.hookX][this.hookY];
               j = this.hookY + ((j & 8) == 0 ? -1 : 1);
               if (objectMap[this.hookX + i][j] >= 0) {
                  int l = this.playerX;

                  while (true) {
                     j = l - i;
                     if (objectMap[j][this.playerY] != 32) {
                        this.hookPullSteps = 0;
                        return;
                     }

                     objectMap[j][this.playerY] = -1;
                     l = j;
                  }
               }

               objectMap[this.hookX + i][j] = objectMap[this.hookX][j];
               objectMap[this.hookX][j] = -1;
               objectState[this.hookX + i][j] = objectState[this.hookX][j];
               objectTimers[this.hookX][j] = this.hookPullDelay;
            }

            objectMap[this.hookX + i][this.hookY] = objectMap[this.hookX][this.hookY];
            objectMap[this.hookX][this.hookY] = -1;
            this.refreshBackgroundTile(this.hookX, this.hookY);
            this.hookX += i;
            objectState[this.hookX][this.hookY] = this.hookedObjectState | -2147483648;
            this.hookPullDelay = 18;
            objectTimers[this.hookX][this.hookY] = this.hookPullDelay;
         } else {
            if (objectMap[this.hookX][this.hookY] == 48) {
               int k = objectState[this.hookX][this.hookY];
               k = this.hookY + ((k & 8) == 0 ? -1 : 1);
               objectTimers[this.hookX][k] = 0;
            } else {
               objectState[this.hookX][this.hookY] = this.hookRestoreState;
            }

            objectTimers[this.hookX][this.hookY] = 0;
            this.hookRestoreState = -1;
            this.redrawTile(this.hookX, this.hookY);
         }

         markAreaDirty(this.hookX - i, this.hookY);
      } else {
         this.hookPullDelay = (byte)(this.hookPullDelay - 6);
         objectTimers[this.hookX][this.hookY] = this.hookPullDelay;
         if ((this.hookPullSteps == 1 || this.hookPullSteps == -1) && this.hookPullDelay <= 6 && (this.playerFlags & 112) == 0) {
            this.setPlayerAnim((this.playerFlags & 7) == 4 ? 23 : 21);
         }
      }
   }

   private void updateCompass() {
      int i = compassTargets[this.nextCompassTarget << 1] - this.playerX;
      int j = compassTargets[(this.nextCompassTarget << 1) + 1] - this.playerY;
      if (this.stageKind == 2 && this.nextCompassTarget == 2) {
         i = 10;
         j = -8;
      }

      GameCanvas gamecanvas;
      byte b0;
      if (j == 0) {
         if (i < 0) {
            gamecanvas = this;
            b0 = 12;
         } else {
            gamecanvas = this;
            b0 = 4;
         }
      } else if (i == 0) {
         if (j < 0) {
            gamecanvas = this;
            b0 = 0;
         } else {
            gamecanvas = this;
            b0 = 8;
         }
      } else if ((j = (i << 7) / j) > 0) {
         if (j < 128) {
            if (i > 0) {
               gamecanvas = this;
               b0 = 7;
            } else {
               gamecanvas = this;
               b0 = 15;
            }
         } else if (j > 128) {
            if (i > 0) {
               gamecanvas = this;
               b0 = 5;
            } else {
               gamecanvas = this;
               b0 = 13;
            }
         } else if (i > 0) {
            gamecanvas = this;
            b0 = 6;
         } else {
            gamecanvas = this;
            b0 = 14;
         }
      } else if (j > -128) {
         if (i < 0) {
            gamecanvas = this;
            b0 = 9;
         } else {
            gamecanvas = this;
            b0 = 1;
         }
      } else if (j < -128) {
         if (i < 0) {
            gamecanvas = this;
            b0 = 11;
         } else {
            gamecanvas = this;
            b0 = 3;
         }
      } else if (i < 0) {
         gamecanvas = this;
         b0 = 10;
      } else {
         gamecanvas = this;
         b0 = 2;
      }

      gamecanvas.compassDirection = b0;
   }

   private void hurtPlayerNear(int var1, int var2) {
      for (int i = -1; i < 2; i++) {
         for (int j = -1; j < 2; j++) {
            int k = (this.cameraX + var1) / 24 + j;
            int l = (this.cameraY + var2) / 24 + i;
            if (this.isPlayerAt(k, l)) {
               this.hurtPlayer(1, 48, 0);
            }
         }
      }
   }

   private void checkBossContact() {
      int i = this.bossColumn();
      if (this.playerX == i || this.playerX == i + 1) {
         SpriteInstance spriteinstance;
         int j = (spriteinstance = spriteInstances[5]).y + -40;
         int k = spriteinstance.y + 256;
         int l = (spriteinstance.sprite.animFrameStarts[spriteinstance.anim] + spriteinstance.animFrame) * 5;
         byte b0 = spriteinstance.sprite.animFrames[l + 3];
         j -= b0;
         k -= b0;
         int i1 = spriteInstances[0].y;
         if (spriteInstances[0].y > j && i1 < k && !this.bossSlamHitPlayer) {
            this.hurtPlayer(1, 48, this.playerX == i ? 4 : 2);
         }
      }
   }

   private static void clearObjectPair(int var0, int var1) {
      if (var0 >= 0 && var1 >= 0) {
         objectMap[var0][var1] = -1;
         objectMap[var0 + 1][var1] = -1;
      }
   }

   private static void fillObjectPair(int var0, int var1) {
      if (var0 >= 0 && var1 >= 0) {
         objectMap[var0][var1] = 50;
         objectMap[var0 + 1][var1] = 50;
      }
   }

   private boolean crushObjectsAt(int var1, int var2) {
      boolean flag = false;

      for (int i = var1; i <= var1 + 1; i++) {
         for (int j = 8; j >= 7; j--) {
            if (objectMap[i][j] == 0) {
               this.smashObject(i, j);
               markAreaDirty(i, j);
               objectMap[i][j] = -1;
               this.refreshBackgroundTile(i, j);
               flag = true;
            }
         }
      }

      return flag;
   }

   private int bossColumn() {
      int i = this.bossPosition > 0 ? 1 : 0;
      return 10 + this.bossPosition * (i + 2);
   }

   private int bossRow() {
      SpriteInstance spriteinstance;
      int i = (spriteinstance = spriteInstances[5]).y + -40;
      int j = (spriteinstance.sprite.animFrameStarts[spriteinstance.anim] + spriteinstance.animFrame) * 5;
      byte b0 = spriteinstance.sprite.animFrames[j + 3];
      i = i - b0 + this.cameraY;
      return this.mapHeight * i / (this.mapHeight * 24);
   }

   private boolean crushObjectsUnderBoss() {
      int i = this.bossColumn();
      int j = this.bossRow();
      boolean flag = false;

      for (int k = i; k <= i + 1; k++) {
         for (int l = j; l <= 10; l++) {
            if (objectMap[k][l] == 0) {
               this.smashObject(k, l);
               markAreaDirty(k, l);
               objectMap[k][l] = -1;
               this.refreshBackgroundTile(k, l);
               flag = true;
            }
         }
      }

      return flag;
   }

   private void restoreArenaWalls() {
      this.screenShakeTicks = 30;
      if (objectMap[this.arenaWallX1][this.arenaWallRow2] == -1) {
         objectMap[this.arenaWallX1][this.arenaWallRow] = 0;
         markAreaDirty(this.arenaWallX1, this.arenaWallRow);
      }

      if (objectMap[this.arenaWallX2][this.arenaWallRow2] == -1) {
         objectMap[this.arenaWallX2][this.arenaWallRow] = 0;
         markAreaDirty(this.arenaWallX2, this.arenaWallRow);
      }
   }

   private void resetBoss() {
      this.bossAnim = -1;
      this.bossHealth = 5;
   }

   public final void updateCamera() {
      byte b0;
      int j;
      int k;
      label58: {
         b0 = (this.playerFlags & 4096) == 0 ? this.queuedAction : 0;
         int i = this.playerX * 24 + this.moveProgress * directionOffsets[b0];
         j = 24 * this.mapWidth - 320;
         k = 24 * this.mapHeight - 178;
         GameCanvas gamecanvas3;
         int j1;
         if (i < this.cameraTargetX + 144) {
            this.cameraTargetX = this.cameraTargetX - 144 + i >> 1;
            if (this.cameraTargetX >= 0) {
               break label58;
            }

            gamecanvas3 = this;
            j1 = 0;
         } else {
            if (i <= this.cameraTargetX + 152) {
               break label58;
            }

            this.cameraTargetX = this.cameraTargetX - 152 + i >> 1;
            if (this.cameraTargetX <= j) {
               break label58;
            }

            gamecanvas3 = this;
            j1 = j;
         }

         gamecanvas3.cameraTargetX = j1;
      }

      int l;
      if ((l = this.playerY * 24 + this.moveProgress * directionOffsets[b0 + 8] + 31) < this.cameraTargetY + 144) {
         this.cameraTargetY = this.cameraTargetY - 144 + l >> 1;
         if (this.cameraTargetY < 0) {
            this.cameraTargetY = 0;
         }
      }

      if (l > this.cameraTargetY + 41) {
         this.cameraTargetY = this.cameraTargetY - 41 + l >> 1;
         if (this.cameraTargetY > k) {
            this.cameraTargetY = k;
         }
      }

      label45: {
         this.cameraX = this.cameraTargetX;
         this.cameraY = this.cameraTargetY;
         GameCanvas gamecanvas;
         int i1;
         if (this.cameraX < 0) {
            gamecanvas = this;
            i1 = 0;
         } else {
            if (this.cameraX <= j) {
               break label45;
            }

            gamecanvas = this;
            i1 = j;
         }

         gamecanvas.cameraX = i1;
      }

      GameCanvas gamecanvas1;
      GameCanvas gamecanvas2;
      int k1;
      if (this.cameraY < 0) {
         gamecanvas1 = this;
         gamecanvas2 = this;
         k1 = 0;
      } else {
         if (this.cameraY <= k) {
            return;
         }

         gamecanvas1 = this;
         gamecanvas2 = this;
         k1 = k;
      }

      gamecanvas1.cameraY = gamecanvas2.cameraTargetY = k1;
   }

   private void clearTileRegion(int var1, int var2, byte var3) {
      while ((tileMap[var1][var2] & 0xFF) == var3) {
         tileMap[var1][var2] = -1;
         this.clearTileRegion(var1 - 1, var2, var3);
         this.clearTileRegion(var1 + 1, var2, var3);
         this.clearTileRegion(var1, var2 - 1, var3);
         var2++;
         var1 = var1;
      }
   }

   private boolean tryMovePlayer(int var1, int var2, boolean var3) {
      int i = 0;
      int j = this.playerFlags;
      int k = this.playerX;
      int l = this.playerY;
      byte b0 = this.queuedAction;
      boolean flag = this.cheatSkipWalking;
      int i1 = this.moveSpeed;
      int j1 = this.moveProgress;
      int k1 = this.deathTimer;
      int j4;
      int b6;
      if (var1 > 0) {
         j4 = j & -8;
         b6 = 2;
      } else if (var1 < 0) {
         j4 = j & -8;
         b6 = 4;
      } else if (var2 < 0) {
         j4 = j & -8;
         b6 = 1;
      } else {
         j4 = j & -8;
         b6 = 3;
      }

      j = j4 | b6;
      int l1 = k + var1;
      int i2 = l + var2;
      boolean flag1 = false;
      boolean flag2 = false;
      if (l1 >= 0 && l1 < this.mapWidth && i2 >= 0 && i2 < this.mapHeight) {
         if (tileSlots != null && tileSlots[l1][i2] != 0 && saveData[10] == 0) {
            flag1 = false;
            i = 1;
         }

         label334: {
            boolean flag3;
            switch ((byte)tileMap[l1][i2]) {
               case 1:
                  if (!var3) {
                     this.screenShakeTicks = 120;
                     this.floodSwitchPresses++;
                     this.clearTileRegion(l1, i2, (byte)1);
                  }
                  break label334;
               case 2:
                  if (var3) {
                     break label334;
                  }

                  GameCanvas gamecanvas1;
                  GameCanvas gamecanvas2;
                  label329: {
                     label328: {
                        flag2 = true;
                        switch (tileMap[l1][i2] >> 8) {
                           case 0:
                              if (saveData[9] >= 1) {
                                 this.hintIconSet = 7;
                                 gamecanvas2 = this;
                                 b6 = (byte)0;
                                 break label329;
                              }

                              gamecanvas1 = this;
                              break;
                           case 1:
                              if (saveData[9] >= 2) {
                                 gamecanvas1 = this;
                                 b6 = (byte)7;
                                 break label328;
                              }

                              gamecanvas1 = this;
                              break;
                           default:
                              break label334;
                        }

                        b6 = (byte)13;
                     }

                     gamecanvas1.hintIconSet = b6;
                     gamecanvas2 = this;
                     b6 = (byte)1;
                  }

                  gamecanvas2.hintIconFrame = b6;
                  break label334;
               case 3:
                  if (tileMap[l1][i2] >> 8 < 3) {
                     flag1 = true;
                     break label334;
                  }

                  flag1 = false;
                  flag3 = true;
                  break;
               case 4:
                  int i3;
                  if (!var3 && (i3 = (tileMap[l1][i2] & -256) >> 8) >= this.nextCompassTarget) {
                     this.checkpointReached = true;
                     this.nextCompassTarget = i3 + 1;
                     this.checkpointBannerUntil = stateTicks + 13;
                  }
                  break label334;
               case 5:
                  flag1 = true;
                  if (!var3) {
                     flag = true;
                     this.cheatSkipExitA = true;
                  }
               case 6:
               case 8:
               case 9:
               case 10:
               case 11:
               case 12:
               case 13:
               case 14:
               case 15:
               case 16:
               case 17:
               case 18:
               case 20:
               case 21:
               case 22:
               case 23:
               case 24:
               case 25:
               case 26:
               case 27:
               default:
                  break label334;
               case 7:
                  if ((tileMap[l1][i2] >> 8 & 240) >> 4 >= 2) {
                     break label334;
                  }

                  flag3 = true;
                  break;
               case 19:
                  if (var3) {
                     break label334;
                  }

                  int j2 = l1 + 3;
                  int k2 = i2;

                  while (objectMap[j2][k2] != 39) {
                     k2--;
                  }

                  if (!this.hasEnemies) {
                     this.resetEnemies();
                  }

                  this.hasEnemies = true;
                  this.enemyRemovalPending = false;
                  if (tileSlots == null) {
                     tileSlots = new int[this.mapWidth][this.mapHeight];
                  }

                  this.spawnEnemy((byte)objectState[j2][k2], (byte)j2, (byte)k2, (byte)0);
                  this.killEnemyAt(j2, k2);

                  while (true) {
                     if ((tileMap[l1][++k2] & 0xFF) != 19) {
                        break label334;
                     }

                     tileMap[l1][k2] = -1;
                  }
               case 28:
                  if (!var3) {
                     flag = true;
                     this.cheatSkipExitA = false;
                  }
                  break label334;
            }

            i = flag3 ? 1 : 0;
         }

         if (i == 0) {
            byte b4;
            switch (b4 = objectMap[l1][i2]) {
               case -1:
                  label369: {
                     boolean flag4;
                     if (l1 != 0 && i2 != 0 && l1 != this.mapWidth - 1 && i2 != this.mapHeight - 1) {
                        if ((
                              var1 != 0 && objectMap[l1][l + 1] == 0 && (objectState[l1][l + 1] & 7) == 3
                                 || this.trapLengthA >= 24
                                    && (
                                       objectMap[l1][i2 - 1] == 28 && (objectState[l1][i2 - 1] & 8) == 0
                                          || objectMap[l1][i2 + 1] == 28 && (objectState[l1][i2 + 1] & 8) == 0
                                    )
                                 || this.trapLengthB >= 24 && (objectMap[l1][i2 - 1] == 28 || objectMap[l1][i2 + 1] == 28)
                           )
                           && !var3) {
                           b0 = 0;
                           break label369;
                        }

                        flag4 = true;
                     } else {
                        flag4 = true;
                     }

                     flag1 = flag4;
                  }

                  j &= -9;
                  break;
               case 0:
               case 8:
               case 9:
               case 47:
                  int k3 = k + (var1 << 1);
                  var2 = l + (var2 << 1);
                  if (this.stageKind == 4) {
                     i = this.bossColumn();
                     if (k3 == i || k3 == i + 1) {
                        int l3 = this.bossRow();
                        if (var2 >= l3) {
                           j |= 8;
                           this.setPlayerAnim((j & 7) == 2 ? 8 : 9);
                           return false;
                        }
                     }
                  }

                  i1--;
                  i = tileMap[k3][var2] & 0xFF;
                  int i4 = tileMap[k3][var2] >> 8;
                  byte b2 = objectMap[l1][l + 1];
                  if ((i1 < 0 || var3)
                     && var1 != 0
                     && isEmptyFloor(k3, var2)
                     && (i != 7 || (i4 & 240) != 0)
                     && (b2 != 19 && b2 != 45 && b2 != 49 && b2 != 43 || (tileMap[l1][l + 1] & 0xFF) == 35)) {
                     if (!var3) {
                        boolean flag5 = var1 > 0;
                        b4 = b4;
                        flag1 = flag5;
                        int l2 = i2;
                        i = l1;
                        GameCanvas gamecanvas = this;
                        k3 = flag1 ? i + 1 : i - 1;
                        i4 = objectState[i][l2];
                        objectMap[k3][l2] = b4;
                        objectMap[i][l2] = -1;
                        objectTimers[k3][l2] = 18;
                        if (flag1) {
                           j4 = (i4 & -8 | 2) & -3073;
                           b6 = 1024;
                        } else {
                           j4 = (i4 & -8 | 4) & -3073;
                           b6 = 2048;
                        }

                        i4 = j4 | b6;
                        objectState[k3][l2] = i4 & -513 | -2147483648;
                        markAreaDirty(k3, l2);
                        gamecanvas.refreshBackgroundTile(i, l2);
                     }

                     flag1 = true;
                     j |= 8;
                  } else {
                     if (var1 != 0) {
                        j |= 8;
                     }

                     b0 = 0;
                     objectState[l1][i2] = objectState[l1][i2] & -8;
                  }
                  break;
               case 1:
               case 2:
               case 4:
               case 5:
               case 6:
               case 7:
               case 11:
               case 14:
               case 19:
               case 24:
               case 26:
               case 27:
               case 33:
               case 40:
               case 41:
               case 42:
               case 43:
               case 45:
               case 50:
               case 51:
               case 52:
               case 53:
                  flag1 = true;
                  break;
               case 3:
               case 12:
               case 13:
               case 15:
               case 16:
               case 17:
               case 18:
               case 20:
               case 21:
               case 22:
               case 23:
               case 25:
               case 29:
               case 30:
               case 32:
               case 36:
               case 37:
               case 38:
               case 39:
               case 44:
               case 46:
               default:
                  var1 = 0;
                  flag1 = false;
                  if (this.queuedAction == 4 || this.queuedAction == 2) {
                     this.setPlayerAnim(0 + this.queuedAction - 1);
                  }
                  break;
               case 10:
                  if (this.enemyPhase == 3) {
                     if (!var3 && objectState[l1][i2] <= 0) {
                        objectState[l1][i2] = 1;
                     }

                     flag1 = true;
                  }
                  break;
               case 28:
                  b0 = 0;
                  j &= -9;
                  break;
               case 31:
               case 49:
                  flag1 = false;
                  break;
               case 34:
               case 35:
                  flag1 = var3;
                  break;
               case 48:
                  int j3 = k + (var1 << 1);
                  var2 = l + (var2 << 1);
                  i1--;
                  byte b3;
                  byte b5;
                  if ((objectState[l1][i2] & 8) != 0) {
                     b3 = 0;
                     b5 = 1;
                  } else {
                     b3 = -1;
                     b5 = 0;
                  }

                  byte b1 = b5;
                  if ((i1 < 0 || var3) && var1 != 0 && objectMap[j3][var2 + b3] < 0 && objectMap[j3][var2 + b1] < 0 && objectMap[l1][i2 + b1 + 1] >= 0) {
                     if (!var3) {
                        objectMap[j3][var2 + b3] = b4;
                        objectMap[j3][var2 + b1] = b4;
                        objectMap[l1][i2 + b3] = -1;
                        objectMap[l1][i2 + b1] = -1;
                        objectState[j3][var2 + b3] = objectState[l1][i2 + b3];
                        objectState[j3][var2 + b1] = objectState[l1][i2 + b1] & -16 | (var1 > 0 ? 2 : 4);
                        objectTimers[j3][var2 + b1] = 18;
                        tileRedrawTimers[j3][var2 + b3 - 1] = 48;
                        tileRedrawTimers[j3][var2 + b3 - 1] = 48;
                        tileRedrawTimers[j3][var2 + b3 - 1] = 48;
                        markAreaDirty(j3, var2 + b1);
                     }

                     flag1 = true;
                     j |= 8;
                  } else {
                     if (var1 != 0) {
                        j |= 8;
                     }

                     b0 = 0;
                  }
            }
         }
      } else {
         flag1 = true;
      }

      if (!var3 && flag1 && !flag2) {
         this.hintIconSet = -1;
      }

      if (flag1 && k1 == 0) {
         j1 = 18;
         k += var1;
         l = i2;
         if ((j & 8) == 0 && !var3) {
            this.setPlayerAnim(b0 + 4 - 1);
         }
      } else if (var1 != 0 && !var3) {
         j |= 8;
      }

      if ((j & 8) != 0 && !var3) {
         this.setPlayerAnim((j & 7) == 2 ? 8 : 9);
      }

      if (!var3) {
         this.playerFlags = j;
         this.playerX = k;
         this.playerY = l;
         this.queuedAction = b0;
         this.cheatSkipWalking = flag;
         this.moveSpeed = i1;
         this.moveProgress = j1;
         this.deathTimer = k1;
      }

      return flag1;
   }

   public static void requestQuit() {
      gameState = 3;
   }

   private void openPauseMenu() {
      gameState = 2;
      menuNeedsRedraw = true;
      this.openMenu(1);
      this.hudDirty = true;
      if (sprites[18] == null) {
         byte b0 = 3;
         String s = "/ui.f";
         boolean flag = false;
         b0 = b0;
         String s1 = "/ui.f";
         sprites[18] = loadSprite("/ui.f", b0, 0, 0);
      }
   }

   public final void pauseGame() {
      this.pausedAt = System.currentTimeMillis() - this.stateStartTime;
      this.paused = true;
      System.out.println(SoundManager.isPlaying());
      this.sound.stop();
   }

   public final void showNotify() {
      if (this.paused) {
         this.resumeGame();
      }
   }

   public final void hideNotify() {
      if (!this.paused) {
         this.pauseGame();
      }
   }

   public final void resumeGame() {
      this.paused = false;
      menuNeedsRedraw = true;
      pauseBackgroundStale = true;
      this.keyReleased = true;
      keysDown = 0;
      this.menuAnimFrame = -1;
      if (this.menu == 7) {
         this.menuSelection = 0;
      }

      this.stateStartTime = System.currentTimeMillis() - this.pausedAt;
      GameCanvas gamecanvas;
      switch (gameState) {
         case 0:
         case 6:
         case 12:
         case 22:
            return;
         case 1:
            this.screen.setClip(0, 0, 320, 240);
            this.screen.setColor(0);
            this.screen.fillRect(0, 0, 320, 240);
            this.hudDirty = true;
            if (pickupAnimStarting) {
               this.hudDirty = true;
               this.paintHud();
               return;
            }

            if (this.activeCutscene != null) {
               return;
            }

            if (this.entryWalkSteps != 0) {
               return;
            }

            if (this.cheatSkipWalking) {
               return;
            }

            if (this.energy <= 0) {
               return;
            }

            if (spriteInstances[0].anim == 19) {
               return;
            }

            if (this.cameraPanToCheckpoint) {
               return;
            }

            this.openPauseMenu();
            return;
         case 2:
            mapPreview = null;
            if (pickupAnimStarting) {
               this.hudDirty = true;
               this.paintHud();
            }

            this.hudDirty = true;
            if (this.menu == 1) {
               this.menuSelection = 0;
               return;
            }

            return;
         case 3:
         case 4:
         case 10:
         case 13:
         case 14:
         case 24:
         case 29:
         case 30:
         case 32:
         default:
            return;
         case 5:
            this.tipVisible = true;
            return;
         case 7:
            this.menuSelection = 1;
            return;
         case 8:
            return;
         case 9:
         case 28:
            return;
         case 11:
            this.loadingStep = 6;
            return;
         case 15:
            gamecanvas = this;
            break;
         case 16:
            this.lives = 0;
            this.stageRedDiamonds = 0;
            this.hasCompass = false;
            this.tipIndex = 0;
            this.creatingNewGame = true;
            return;
         case 17:
            return;
         case 18:
         case 25:
         case 26:
            gamecanvas = this;
            break;
         case 19:
            return;
         case 20:
            return;
         case 21:
            this.openWorldMap();
            return;
         case 23:
            return;
         case 27:
            this.fullRedraw = true;
            this.sealDirty = true;
            return;
         case 31:
            this.blackScreenPending = true;
            return;
         case 33:
            gamecanvas = this;
      }

      gamecanvas.fullRedraw = true;
   }

   private void spawnEnemiesFromMap() {
      if (this.hasEnemies) {
         for (int i = 0; i < this.mapWidth; i++) {
            for (int j = 0; j < this.mapHeight; j++) {
               if (objectMap[i][j] == 38) {
                  this.spawnEnemy((byte)objectState[i][j], (byte)i, (byte)j, (byte)0);
                  this.enemyCount++;
               }
            }
         }
      }
   }

   private void placeItemChest(int var1, int var2) {
      this.neededGenSprites |= 4194304L;
      tileMap[var1][var2] = tileMap[var1][var2] & -256;
      tileMap[var1][var2] = tileMap[var1][var2] | 14;
   }

   private boolean isBossStage() {
      return this.stageKind == 4 || this.stageKind == 5 || this.stageKind == 3;
   }

   private void propagateTileFlag(int var1, int var2, int var3, int var4, int var5) {
      while (var1 + var3 > 0 && var1 + var3 < this.mapWidth && var2 + var4 > 0 && var2 + var4 < this.mapHeight) {
         int j;
         switch (j = tileMap[var1 + var3][var2 + var4] & 0xFF) {
            case 8:
            case 9:
               int i = tileMap[var1 + var3][var2 + var4] >> 8;
               i = var5 == 1 ? i | 512 : i & -513;
               tileMap[var1 + var3][var2 + var4] = i << 8 | j;
               this.propagateTileFlag(var1 + var3, var2 + var4, var3, var4, var5);
               int k = var1 + var3;
               int l = var2 + var4;
               var4 = 1;
               var3 = 0;
               var2 = l;
               var1 = k;
               break;
            default:
               return;
         }
      }
   }

   private boolean isPlayerAt(int var1, int var2) {
      return var1 == this.playerX && var2 == this.playerY;
   }

   private void startCutscene(int var1) {
      this.playerAction = this.queuedAction = 0;

      for (int i = 0; i < cutsceneIds.length; i++) {
         if (cutsceneIds[i] == var1) {
            this.activeCutscene = cutscenes[i];
         }
      }

      this.activeCutscene.restart();
   }

   private boolean panCameraToTarget() {
      label61: {
         if (this.cameraX < this.cameraPanX) {
            this.cameraX = this.cameraX + this.cameraPanSpeed;
            if (this.cameraX <= this.cameraPanX) {
               break label61;
            }
         } else {
            if (this.cameraX <= this.cameraPanX) {
               break label61;
            }

            this.cameraX = this.cameraX - this.cameraPanSpeed;
            if (this.cameraX >= this.cameraPanX) {
               break label61;
            }
         }

         this.cameraX = this.cameraPanX;
      }

      label53: {
         if (this.cameraY < this.cameraPanY) {
            this.cameraY = this.cameraY + this.cameraPanSpeed;
            if (this.cameraY <= this.cameraPanY) {
               break label53;
            }
         } else {
            if (this.cameraY <= this.cameraPanY) {
               break label53;
            }

            this.cameraY = this.cameraY - this.cameraPanSpeed;
            if (this.cameraY >= this.cameraPanY) {
               break label53;
            }
         }

         this.cameraY = this.cameraPanY;
      }

      boolean flag;
      boolean flag1;
      label45: {
         flag = false;
         flag1 = false;
         if (this.cameraX < 0) {
            this.cameraX = 0;
         } else if (this.cameraX > this.mapWidth * 24 - 320) {
            this.cameraX = this.mapWidth * 24 - 320;
         } else if (this.cameraX != this.cameraPanX) {
            break label45;
         }

         flag = true;
      }

      label39: {
         if (this.cameraY < 0) {
            this.cameraY = 0;
         } else if (this.cameraY > this.mapHeight * 24 - 240 + 62) {
            this.cameraY = this.mapHeight * 24 - 240 + 62;
         } else if (this.cameraY != this.cameraPanY) {
            break label39;
         }

         flag1 = true;
      }

      if (flag && flag1) {
         this.cameraTargetX = this.cameraX;
         this.cameraTargetY = this.cameraY;
         this.idleAnimDelay = 70;
         return true;
      } else {
         return false;
      }
   }

   private void updateSwitchReveal() {
      switch (this.switchRevealPhase) {
         case 1:
            this.cameraPanSpeed = 8;
            if (this.panCameraToTarget()) {
               this.switchRevealPhase = 2;
               this.switchRevealTimer = 40;
               return;
            }
            break;
         case 2:
            this.switchRevealTimer--;
            if (this.switchRevealTimer == 30) {
               if ((tileMap[this.switchTargetX][this.switchTargetY] >> 8 & 240) != 0) {
                  this.closeGateAt(this.switchTargetX, this.switchTargetY);
                  return;
               }
            } else if (this.switchRevealTimer == 0) {
               this.switchRevealPhase = 3;
               this.cameraPanX = this.playerX * 24 - 148;
               this.cameraPanY = this.playerY * 24 - 77;
               this.cameraPanSpeed = 5;
               this.hintText = strings[switchTargetObjects[activeSwitchId]];
               fontMedium.getTextHeight(this.hintText);
               this.hintTextTicks = 80;
               return;
            }
            break;
         case 3:
            if (this.panCameraToTarget()) {
               this.switchRevealTimer = 20;
               this.switchRevealPhase = 4;
               this.idleAnimDelay = 0;
               return;
            }
            break;
         case 4:
            this.switchRevealTimer--;
            if (this.switchRevealTimer == 0) {
               this.idleAnimDelay = 0;
               this.switchRevealPhase = 0;
               cameraHeldAfterReveal = true;
            }
      }
   }

   private void updateTilesNearPlayer() {
      int i = this.playerX - 8;
      int j = this.playerX + 8;
      int k = this.playerY + 8;
      int l = this.playerY - 8;
      byte b0 = 0;
      if (spriteInstances[4] != null) {
         int i1 = spriteInstances[4].animFrame;
         b0 = (byte)(spriteInstances[4].animFrame == 0 ? 0 : (i1 <= 10 ? 1 : (i1 <= 20 ? 2 : 3)));
      }

      if (i < 1) {
         i = 1;
      }

      if (j > this.mapWidth - 2) {
         j = this.mapWidth - 2;
      }

      if (l < 1) {
         l = 1;
      }

      if (k > this.mapHeight - 2) {
         k = this.mapHeight - 2;
      }

      GameCanvas gamecanvas22 = this;
      int k22 = k;

      while (true) {
         gamecanvas22.scanY = k22;
         if (this.scanY < l) {
            return;
         }

         gamecanvas22 = this;
         k22 = i;

         while (true) {
            gamecanvas22.scanX = k22;
            if (this.scanX > j) {
               gamecanvas22 = this;
               k22 = this.scanY - 1;
               break;
            }

            int i3 = this.hookPullSteps != 0 && this.scanX == this.hookX && this.scanY == this.hookY ? 1 : 0;
            label768:
            if (tileRedrawTimers[this.scanX][this.scanY] > 0 && i3 == 0) {
               label1156: {
                  tileRedrawTimers[this.scanX][this.scanY] = (byte)(tileRedrawTimers[this.scanX][this.scanY] - 6);
                  switch ((byte)tileMap[this.scanX][this.scanY]) {
                     case 0:
                        if (this.activeCutscene == null && this.pendingHint == -1 && this.isPlayerAt(this.scanX, this.scanY) && this.moveProgress <= 6) {
                           this.pendingHint = tileMap[this.scanX][this.scanY] >> 8;
                           tileMap[this.scanX][this.scanY] = -1;
                        }
                     case 1:
                     case 4:
                     case 5:
                     case 10:
                     case 11:
                     case 12:
                     case 13:
                     case 15:
                     case 16:
                     case 17:
                     case 18:
                     case 19:
                     case 20:
                     case 21:
                     case 22:
                     case 23:
                     case 24:
                     case 25:
                     case 27:
                     case 28:
                     case 29:
                     case 31:
                     case 34:
                     default:
                        break label1156;
                     case 2:
                        switch (tileMap[this.scanX][this.scanY] >> 8) {
                           case 0:
                              if (objectMap[this.scanX - 1][this.scanY] != 30
                                 && objectMap[this.scanX + 1][this.scanY] != 30
                                 && objectMap[this.scanX][this.scanY - 1] != 30
                                 && objectMap[this.scanX][this.scanY + 1] != 30) {
                                 this.hintIconSet = -1;
                                 this.clearTileRegion(this.scanX, this.scanY, (byte)2);
                              }
                           default:
                              break label1156;
                        }
                     case 3:
                        GameCanvas gamecanvas7 = this;
                        i3 = this.scanX;
                        int k4 = gamecanvas7.scanY;
                        int k8;
                        if ((k8 = tileMap[i3][k4] >> 8) < 6) {
                           label1079:
                           switch (k8) {
                              case -1:
                                 if (Math.abs(gamecanvas7.playerX - i3) < 4 && Math.abs(gamecanvas7.playerY - k4) < 4) {
                                    k8 = 3;
                                 }
                              case 0:
                              case 1:
                                 break;
                              case 2:
                                 switch (gamecanvas7.queuedAction) {
                                    case 1:
                                       if (!gamecanvas7.isPlayerAt(i3, k4 - 1)) {
                                          break label1079;
                                       }
                                       break;
                                    case 2:
                                       if (!gamecanvas7.isPlayerAt(i3 + 1, k4)) {
                                          break label1079;
                                       }
                                       break;
                                    case 3:
                                       if (!gamecanvas7.isPlayerAt(i3, k4 + 1)) {
                                          break label1079;
                                       }
                                       break;
                                    case 4:
                                       if (!gamecanvas7.isPlayerAt(i3 - 1, k4)) {
                                          break label1079;
                                       }
                                       break;
                                    default:
                                       break label1079;
                                 }

                                 k8 = 3;
                                 break;
                              default:
                                 if ((stateTicks & 1) == 0) {
                                    k8++;
                                 }
                           }

                           tileRedrawTimers[i3][k4] = 24;
                           tileMap[i3][k4] = k8 << 8 | 3;
                        } else {
                           tileRedrawTimers[i3][k4] = 0;
                        }
                        break label1156;
                     case 6:
                        GameCanvas gamecanvas6 = this;
                        i3 = this.scanX;
                        int j4 = gamecanvas6.scanY;
                        int j8 = tileMap[i3][j4] >> 8;
                        boolean flag = isHeavyObject(i3, j4) || objectMap[i3][j4] == 47 || objectMap[i3][j4] == 48;
                        byte b9 = objectTimers[i3][j4];
                        if (!flag && gamecanvas6.isPlayerAt(i3, j4)) {
                           flag = true;
                           b9 = (byte)((gamecanvas6.playerFlags & 4096) != 0 ? 0 : gamecanvas6.moveProgress);
                        }

                        if (flag && b9 < 12) {
                           gamecanvas6.activateGate(j8);
                        } else {
                           gamecanvas6.closeGateById(j8);
                        }
                        break label1156;
                     case 7:
                        GameCanvas gamecanvas5 = this;
                        i3 = this.scanX;
                        int i4 = gamecanvas5.scanY;
                        int i8;
                        int k11;
                        if ((k11 = ((i8 = tileMap[i3][i4] >> 8) & 240) >> 4) != 0) {
                           if (stateTicks % 3 == 0 && k11 < 3) {
                              i8 = i8 & -241 | k11 + 1 << 4;
                              if (k11 == 2) {
                                 int l13;
                                 if ((l13 = tileMap[i3][i4 - 1] & 0xFF) == 9 || l13 == 8) {
                                    int j16 = tileMap[i3][i4 - 1] >> 8 & -513;
                                    tileMap[i3][i4 - 1] = j16 << 8 | l13;
                                 }

                                 gamecanvas5.propagateTileFlag(i3, i4 - 1, 1, 0, 0);
                                 gamecanvas5.propagateTileFlag(i3, i4 - 1, -1, 0, 0);
                              }

                              tileRedrawTimers[i3][i4] = 24;
                           }

                           tileMap[i3][i4] = i8 << 8 | 7;
                        }
                        break label1156;
                     case 8:
                        gamecanvas22 = this;
                        break;
                     case 9:
                        gamecanvas22 = this;
                        break;
                     case 14:
                        this.updateItemChest(14);
                        break label1156;
                     case 26:
                        GameCanvas gamecanvas3 = this;
                        if (this.moveProgress <= 6 && gamecanvas3.isPlayerAt(gamecanvas3.scanX, gamecanvas3.scanY)) {
                           keysDown = 0;
                           gamecanvas3.playerAction = 0;
                           activeSwitchId = tileMap[gamecanvas3.scanX][gamecanvas3.scanY] >> 8;
                           gamecanvas3.closeGateAt(gamecanvas3.playerX + directionOffsets[gamecanvas3.playerFlags & 7], gamecanvas3.playerY);
                           if (activeSwitchId >= 0 && activeSwitchId < switchStates.length) {
                              gamecanvas3.playSound(1);
                              gamecanvas3.switchRevealPhase = 1;
                              int k3 = activeSwitchId;
                              GameCanvas gamecanvas21 = gamecanvas3;
                              gamecanvas3.playSound(1);
                              int k7 = gamecanvas21.mapWidth - 1;
                              int i11 = gamecanvas21.mapHeight - 1;

                              for (int j13 = 1; j13 < i11; j13++) {
                                 for (int i16 = 1; i16 < k7; i16++) {
                                    if ((tileMap[i16][j13] & 0xFF) == 17 && tileMap[i16][j13] >> 8 == k3) {
                                       int i2;
                                       int j2;
                                       label1105: {
                                          i2 = -1;
                                          j2 = 0;
                                          int k21;
                                          if (objectMap[i16][j13] == 18) {
                                             i2 = i16;
                                             k21 = j13;
                                          } else {
                                             switch (tileMap[i16][j13 - 1] & 0xFF) {
                                                case 7:
                                                   if ((tileMap[i16][j13 - 1] >> 8 & 240) == 0) {
                                                      break label1105;
                                                   }
                                                case 14:
                                                case 33:
                                                   i2 = i16;
                                                   k21 = j13 - 1;
                                                   break;
                                                default:
                                                   break label1105;
                                             }
                                          }

                                          j2 = k21;
                                       }

                                       if (i2 != -1) {
                                          gamecanvas21.switchTargetX = i2;
                                          gamecanvas21.switchTargetY = j2;
                                          gamecanvas21.cameraPanX = i2 * 24 - 148;
                                          gamecanvas21.cameraPanY = j2 * 24 - 77;
                                       }
                                    }
                                 }
                              }
                           } else {
                              activeSwitchId = -1;
                           }

                           tileMap[gamecanvas3.scanX][gamecanvas3.scanY] = -1;
                        }
                        break label1156;
                     case 30:
                        if (this.activeCutscene == null && this.pendingHint == -1 && this.isPlayerAt(this.scanX, this.scanY) && this.moveProgress <= 0) {
                           this.pendingHint = tileMap[this.scanX][this.scanY] >> 8;
                           tileMap[this.scanX][this.scanY] = -1;
                        }
                        break label1156;
                     case 32:
                        GameCanvas gamecanvas2 = this;
                        i3 = tileMap[gamecanvas2.scanX][gamecanvas2.scanY] >> 8 & 0xFF;
                        if ((stateTicks & 1) == 0) {
                           i3++;
                        } else if (i3 == 1) {
                           gamecanvas2.refreshBackgroundTile(gamecanvas2.scanX, gamecanvas2.scanY);
                        }

                        int[] aint3;
                        int i22;
                        if (i3 == sprites[16].getAnimFrameCount(0)) {
                           aint3 = tileMap[gamecanvas2.scanX];
                           k22 = gamecanvas2.scanY;
                           i22 = -1;
                        } else {
                           aint3 = tileMap[gamecanvas2.scanX];
                           k22 = gamecanvas2.scanY;
                           i22 = i3 << 8 | 32;
                        }

                        aint3[k22] = i22;
                        tileRedrawTimers[gamecanvas2.scanX][gamecanvas2.scanY] = 24;
                        break label1156;
                     case 33:
                        this.updateItemChest(33);
                        break label1156;
                     case 35:
                     case 37:
                        if (this.scanX != this.hookX || this.scanY != this.hookY || this.hookPullSteps == 0) {
                           GameCanvas gamecanvas1 = this;
                           i3 = this.scanX;
                           int j3 = gamecanvas1.scanY;
                           tileRedrawTimers[i3][j3] = 24;
                           byte[] abyte;
                           byte b25;
                           if (objectTimers[i3][j3] <= 0) {
                              int k1;
                              int l1;
                              byte b1;
                              label1252: {
                                 k1 = j3 - 1;
                                 l1 = j3 + 1;
                                 int[] aint1;
                                 if ((b1 = (byte)tileMap[i3][k1]) != 34 && b1 != 37) {
                                    if (b1 == 35 || !canHoldObject(i3, k1)) {
                                       break label1252;
                                    }

                                    objectTimers[i3][k1] = 18;
                                    aint1 = tileMap[i3];
                                    k22 = k1;
                                    b25 = 35;
                                 } else {
                                    aint1 = tileMap[i3];
                                    k22 = k1;
                                    b25 = 37;
                                 }

                                 aint1[k22] = b25;
                              }

                              byte b2 = objectMap[i3][j3];
                              if (objectMap[i3][k1] < 0 && !gamecanvas1.isPlayerAt(i3, k1) && b1 == 35 && b2 != 32 && b2 != 21 && objectMap[i3][j3] != -1) {
                                 objectTimers[i3][k1] = 18;
                                 objectMap[i3][k1] = objectMap[i3][j3];
                                 objectState[i3][k1] = objectState[i3][j3] & -8 | 1;
                                 objectMap[i3][j3] = -1;
                                 gamecanvas1.refreshBackgroundTile(i3, j3);
                              } else {
                                 gamecanvas1.redrawTile(i3, j3);
                              }

                              if ((tileMap[i3][l1] & 0xFF) != 35 && objectMap[i3][l1] != 47) {
                                 int[] aint2;
                                 if (tileMap[i3][j3] == 37) {
                                    aint2 = tileMap[i3];
                                    k22 = j3;
                                    b25 = 34;
                                 } else {
                                    aint2 = tileMap[i3];
                                    k22 = j3;
                                    b25 = -1;
                                 }

                                 aint2[k22] = b25;
                              }

                              tileRedrawTimers[i3][k1] = 24;
                              if (objectMap[i3][j3] >= 0) {
                                 break label1156;
                              }

                              abyte = objectTimers[i3];
                              k22 = j3;
                              b25 = 18;
                           } else {
                              abyte = objectTimers[i3];
                              k22 = j3;
                              b25 = (byte)(abyte[j3] - 6);
                           }

                           abyte[k22] = b25;
                        }
                        break label1156;
                     case 36:
                        GameCanvas gamecanvas = this;
                        int j1 = tileMap[gamecanvas.scanX][gamecanvas.scanY] >> 8;
                        int[] aint;
                        int l22;
                        if (++j1 >= 16) {
                           aint = tileMap[gamecanvas.scanX];
                           k22 = gamecanvas.scanY;
                           l22 = -1;
                        } else {
                           tileRedrawTimers[gamecanvas.scanX][gamecanvas.scanY] = 24;
                           aint = tileMap[gamecanvas.scanX];
                           k22 = gamecanvas.scanY;
                           l22 = j1 << 8 | 36;
                        }

                        aint[k22] = l22;
                        break label1156;
                  }

                  GameCanvas gamecanvas4 = gamecanvas22;
                  i3 = gamecanvas22.scanX;
                  int l3 = gamecanvas4.scanY;
                  int l7 = tileMap[i3][l3] >> 8;
                  int j11 = tileMap[i3][l3] & 0xFF;
                  if ((l7 & 256) == 0
                     && (j11 == 9 && gamecanvas4.goldKeys > 0 || j11 == 8 && gamecanvas4.silverKeys > 0)
                     && gamecanvas4.playerY == l3
                     && (gamecanvas4.playerX == i3 - 1 || gamecanvas4.playerX == i3 + 1)) {
                     int k13 = spriteInstances[0].anim;
                     if (spriteInstances[0].anim != 18 && k13 != 17 && gamecanvas4.moveProgress <= 6) {
                        gamecanvas4.queuedAction = gamecanvas4.playerAction = 0;
                        byte b23;
                        if (gamecanvas4.playerX == i3 - 1) {
                           gamecanvas4.playerFlags = gamecanvas4.playerFlags & -8 | 2;
                           gamecanvas22 = gamecanvas4;
                           b23 = 18;
                        } else {
                           gamecanvas4.playerFlags = gamecanvas4.playerFlags & -8 | 4;
                           gamecanvas22 = gamecanvas4;
                           b23 = 17;
                        }

                        gamecanvas22.setPlayerAnim(b23);
                        if (j11 == 9) {
                           gamecanvas22 = gamecanvas4;
                           b23 = 24;
                        } else {
                           gamecanvas22 = gamecanvas4;
                           b23 = 25;
                        }

                        gamecanvas22.pickupIconSet = b23;
                        gamecanvas4.pickupIconFrame = 0;
                     }
                  }
               }

               if ((tileMap[this.scanX][this.scanY] & -268435456) >> 28 > 0) {
                  GameCanvas gamecanvas8 = this;
                  if ((i3 = (tileMap[gamecanvas8.scanX][gamecanvas8.scanY] & -268435456) >> 28) == 0) {
                     gamecanvas8.playSound(10);
                  }

                  if ((stateTicks & 1) == 0) {
                     i3++;
                  }

                  int[] aint4;
                  int j22;
                  if (i3 >= sprites[13].getAnimFrameCount(0)) {
                     aint4 = tileMap[gamecanvas8.scanX];
                     k22 = gamecanvas8.scanY;
                     j22 = tileMap[gamecanvas8.scanX][gamecanvas8.scanY] & 268435455;
                  } else {
                     aint4 = tileMap[gamecanvas8.scanX];
                     k22 = gamecanvas8.scanY;
                     j22 = tileMap[gamecanvas8.scanX][gamecanvas8.scanY] & 268435455 | i3 << 28;
                  }

                  aint4[k22] = j22;
                  markAreaDirty(gamecanvas8.scanX, gamecanvas8.scanY);
               }

               int b24;
               switch (objectMap[this.scanX][this.scanY]) {
                  case 0:
                     this.updateObjectAtScan();
                     break label768;
                  case 1:
                     this.updateObjectAtScan();
                     break label768;
                  case 2:
                     this.updatePickup(2);
                  case 3:
                  case 12:
                  case 13:
                  case 15:
                  case 17:
                  case 18:
                  case 20:
                  case 25:
                  case 29:
                  case 31:
                  case 33:
                  case 34:
                  case 35:
                  case 38:
                  case 39:
                  default:
                     break label768;
                  case 4:
                     this.updatePickup(4);
                     break label768;
                  case 5:
                     this.updatePickup(5);
                     break label768;
                  case 6:
                     this.updatePickup(6);
                     break label768;
                  case 7:
                     this.updatePickup(7);
                     break label768;
                  case 8:
                     this.updateObjectAtScan();
                     break label768;
                  case 9:
                     label1036:
                     if ((objectState[this.scanX][this.scanY] & 264241152) == 138412032) {
                        label1034: {
                           tileRedrawTimers[this.scanX][this.scanY] = 24;
                           if (this.shakeX > 0) {
                              gamecanvas22 = this;
                              b24 = this.shakeX - 1;
                           } else {
                              if (this.shakeX >= 0) {
                                 break label1034;
                              }

                              gamecanvas22 = this;
                              b24 = this.shakeX + 1;
                           }

                           gamecanvas22.shakeX = b24;
                        }

                        if (this.shakeY > 0) {
                           gamecanvas22 = this;
                           b24 = this.shakeY - 1;
                        } else {
                           if (this.shakeY >= 0) {
                              break label1036;
                           }

                           gamecanvas22 = this;
                           b24 = this.shakeY + 1;
                        }

                        gamecanvas22.shakeY = b24;
                     }

                     this.updateObjectAtScan();
                     break label768;
                  case 10:
                     GameCanvas gamecanvas20 = this;
                     i3 = this.scanX;
                     int j7 = gamecanvas20.scanY;
                     if (objectState[i3][j7] > 0) {
                        objectMap[i3][j7] = -1;
                        tileMap[i3][j7] = 32;
                        markAreaDirty(i3, j7);
                        gamecanvas20.alertCreatures(i3, j7);
                        tileRedrawTimers[i3][j7] = 24;
                     }
                     break label768;
                  case 11:
                     this.updateObject11();
                     break label768;
                  case 14:
                     GameCanvas gamecanvas19 = this;
                     i3 = this.scanX;
                     int i7 = gamecanvas19.scanY;
                     int l10;
                     int i13 = (l10 = objectState[i3][i7]) >> 8 & 0xFF;
                     int l15 = (l10 & 8) != 0 ? 4 : 2;
                     if (i13 >= 20) {
                        if (isFreeTile(i3, i7 + 1)
                           || l15 == 4
                              && (objectMap[i3 - 1][i7] < 0 || objectMap[i3 - 1][i7] == 16 || objectMap[i3 - 1][i7] == 19 || objectMap[i3 - 1][i7] == 43)
                           || l15 == 2
                              && (objectMap[i3 + 1][i7] < 0 || objectMap[i3 + 1][i7] == 16 || objectMap[i3 + 1][i7] == 19 || objectMap[i3 + 1][i7] == 43)) {
                           objectState[i3][i7] = l10 & -65281 | 4864;
                        }
                     } else {
                        label1002: {
                           byte[] abyte5;
                           byte b29;
                           if (i13 > 0) {
                              objectState[i3][i7] = l10 & -65281 | i13 - 1 << 8;
                              abyte5 = tileRedrawTimers[i3];
                              b24 = i7;
                              b29 = 24;
                           } else {
                              byte b14;
                              if ((b14 = objectTimers[i3][i7]) <= 0) {
                                 int i19 = i3;
                                 int j20 = i7;
                                 if (isFreeTile(i3, i7 + 1)) {
                                    j20 = i7 + 1;
                                    l15 = 3;
                                 } else {
                                    label1233: {
                                       if (l15 == 4) {
                                          if (isFreeTile(i3 - 1, i7)) {
                                             i19 = i3 - 1;
                                             break label1233;
                                          }

                                          l15 = 0;
                                          if (objectMap[i3 - 1][i7] == 16 || objectMap[i3 - 1][i7] == 19 || objectMap[i3 - 1][i7] == 43) {
                                             break label1233;
                                          }
                                       } else {
                                          if (isFreeTile(i3 + 1, i7)) {
                                             i19 = i3 + 1;
                                             break label1233;
                                          }

                                          l15 = 0;
                                          if (objectMap[i3 + 1][i7] == 16 || objectMap[i3 + 1][i7] == 19 || objectMap[i3 + 1][i7] == 43) {
                                             break label1233;
                                          }
                                       }

                                       l10 = l10 & -65281 | 5120;
                                    }
                                 }

                                 if (i19 != i3 || j20 != i7) {
                                    objectMap[i19][j20] = 14;
                                    markAreaDirty(i19, j20);
                                    objectMap[i3][i7] = -1;
                                    objectTimers[i19][j20] = 18;
                                 }

                                 objectState[i19][j20] = l10 & -8 | l15;
                                 break label1002;
                              }

                              abyte5 = objectTimers[i3];
                              b24 = i7;
                              b29 = (byte)(b14 - 6);
                           }

                           abyte5[b24] = b29;
                        }
                     }

                     if (gamecanvas19.isPlayerAt(i3, i7)) {
                        gamecanvas19.hurtPlayer(1, 48, l15);
                     }
                     break label768;
                  case 16:
                     GameCanvas gamecanvas18 = this;
                     i3 = this.scanX;
                     int l6 = gamecanvas18.scanY;
                     byte b6;
                     byte b22;
                     if (objectMap[i3][l6 + 1] != 16) {
                        b6 = 0;
                        b22 = -1;
                     } else {
                        b6 = 1;
                        b22 = 0;
                     }

                     byte b8 = b22;
                     int b11 = objectTimers[i3][l6 + b6];
                     int i17;
                     int l18 = ((i17 = objectState[i3][l6 + b6]) & 7) == 4 ? 4 : 2;
                     boolean flag3 = gamecanvas18.isPlayerAt(i3 - directionOffsets[l18], l6 + b6)
                        || gamecanvas18.isPlayerAt(i3 - directionOffsets[l18], l6 + b8);
                     if (b11 <= 0 && flag3 && gamecanvas18.moveProgress <= 12) {
                        b11 = 36;
                     } else if (b11 > 0) {
                        if (b6 == 0) {
                           b11--;
                        }

                        if ((b11 == 11 || b6 == 0 && b11 < 11) && flag3) {
                           gamecanvas18.hurtPlayer(1, 48, i17 & 7);
                        }

                        tileRedrawTimers[i3][l6] = 24;
                     }

                     if (isRestingOnSomething(i3, l6)) {
                        gamecanvas18.playSound(14);
                        objectMap[i3][l6 + b8] = -1;
                        gamecanvas18.triggerTile(i3, l6 + b8);
                        objectMap[i3][l6 + b6] = -1;
                        gamecanvas18.triggerTile(i3, l6 + b6);
                     } else {
                        objectState[i3][l6 + b8] = i17;
                        objectState[i3][l6 + b6] = i17;
                        if (b6 == 0) {
                           objectTimers[i3][l6 + b8] = (byte)b11;
                           objectTimers[i3][l6 + b6] = (byte)b11;
                        }
                     }
                     break label768;
                  case 19:
                     this.updateRoller((byte)19);
                     break label768;
                  case 21:
                     GameCanvas gamecanvas17 = this;
                     i3 = this.scanX;
                     int k6 = gamecanvas17.scanY;
                     int k10 = objectState[i3][k6] & 7;
                     byte[] abyte3;
                     byte b28;
                     if ((objectState[i3][k6] & 8) != 0) {
                        byte b21;
                        switch (k10) {
                           case 2:
                              b21 = 13;
                              break;
                           case 4:
                              b21 = 12;
                              break;
                           default:
                              b21 = 14;
                        }

                        byte b7 = b21;
                        if (objectTimers[i3][k6] < animLength(sprites[29], b7) && (tileMap[gamecanvas17.scanX][gamecanvas17.scanY] & 0xFF) != 35) {
                           objectTimers[i3][k6]++;
                        } else {
                           objectMap[i3][k6] = -1;
                           markAreaDirty(i3, k6);
                        }

                        abyte3 = tileRedrawTimers[i3];
                        b24 = k6;
                        b28 = 24;
                     } else {
                        if (gamecanvas17.isPlayerAt(i3, k6)
                           || gamecanvas17.isPlayerAt(i3 + directionOffsets[k10], k6 + directionOffsets[k10 + 8])
                              && objectTimers[i3][k6] <= gamecanvas17.moveProgress) {
                           gamecanvas17.hurtPlayer(1, 48, 0);
                        }

                        if (objectTimers[i3][k6] <= 0) {
                           int l12 = i3 - directionOffsets[k10];
                           int k15 = k6 - directionOffsets[k10 + 8];
                           byte b13 = 24;
                           if (k10 == 4) {
                              b13 = 12;
                           }

                           byte[] abyte4;
                           if (objectMap[l12][k15] < 0) {
                              objectMap[l12][k15] = 21;
                              objectState[l12][k15] = objectState[i3][k6];
                              objectTimers[l12][k15] = b13;
                              abyte4 = objectMap[i3];
                              b24 = k6;
                              b28 = -1;
                           } else if (objectMap[l12][k15] == 21) {
                              int k18 = objectState[l12][k15] & 7;
                              int i20 = l12 - directionOffsets[k18];
                              int j21 = k15 - directionOffsets[k18 + 8];
                              objectMap[i3][k6] = -1;
                              markAreaDirty(i3, k6);
                              k18 = objectState[i3][k6];
                              if (objectMap[i20][j21] < 0) {
                                 objectMap[i20][j21] = 21;
                                 objectState[i20][j21] = objectState[l12][k15];
                                 objectTimers[i20][j21] = 18;
                              }

                              objectMap[l12][k15] = 21;
                              objectState[l12][k15] = k18;
                              abyte4 = objectTimers[l12];
                              b24 = k15;
                              b28 = 18;
                           } else {
                              switch (objectMap[l12][k15]) {
                                 case 10:
                                 case 30:
                                    if (objectState[l12][k15] < 1) {
                                       objectState[l12][k15] = 1;
                                    }
                                    break;
                                 case 19:
                                 case 43:
                                 case 45:
                                 case 46:
                                    objectMap[l12][k15] = -1;
                                    gamecanvas17.triggerTile(l12, k15);
                              }

                              objectState[i3][k6] = objectState[i3][k6] | 8;
                              abyte4 = objectTimers[i3];
                              b24 = k6;
                              b28 = 0;
                           }

                           abyte4[b24] = b28;
                           abyte3 = tileRedrawTimers[l12];
                           b24 = k15;
                           b28 = 48;
                        } else {
                           abyte3 = objectTimers[i3];
                           b24 = k6;
                           b28 = (byte)(abyte3[k6] - 12);
                        }
                     }

                     abyte3[b24] = b28;
                     break label768;
                  case 22:
                     gamecanvas22 = this;
                     b24 = 22;
                     break;
                  case 23:
                     gamecanvas22 = this;
                     b24 = 23;
                     break;
                  case 24:
                     this.collectTreasure(24);
                     break label768;
                  case 26:
                     this.collectTreasure(26);
                     break label768;
                  case 27:
                     this.collectTreasure(27);
                     break label768;
                  case 28:
                     GameCanvas gamecanvas15 = this;
                     int j6 = ((i3 = objectState[gamecanvas15.scanX][gamecanvas15.scanY]) & 7) == 3 ? 1 : -1;
                     int i10 = (i3 & 8) == 0 ? gamecanvas15.trapReachA : gamecanvas15.trapReachB;
                     int i15 = gamecanvas15.scanY + (i10 - 1) * j6;
                     if (gamecanvas15.isPlayerAt(gamecanvas15.scanX, i15)) {
                        gamecanvas15.hurtPlayer(2, 48, oppositeDirection[gamecanvas15.playerFlags & 7]);
                     }

                     switch (objectMap[gamecanvas15.scanX][i15]) {
                        default:
                           gamecanvas15.triggerTile(gamecanvas15.scanX, i15);
                           objectMap[gamecanvas15.scanX][i15] = -1;
                           markAreaDirty(gamecanvas15.scanX, i15);
                           gamecanvas15.refreshBackgroundTile(gamecanvas15.scanX, i15);
                        case -1:
                        case 28:
                        case 32:
                           tileRedrawTimers[gamecanvas15.scanX][gamecanvas15.scanY] = 24;
                           break label768;
                     }
                  case 30:
                     GameCanvas gamecanvas14 = this;
                     if ((i3 = objectState[gamecanvas14.scanX][gamecanvas14.scanY]) <= 0) {
                        break label768;
                     }

                     int i6 = gamecanvas14.scanX;
                     int l9 = gamecanvas14.scanY;
                     if (i3 == 4) {
                        for (int j12 = 1; j12 < 5; j12++) {
                           byte b10 = directionOffsets[j12];
                           byte b12 = directionOffsets[j12 + 8];
                           if (objectMap[i6 + b10][l9 + b12] == 30) {
                              objectState[i6 + b10][l9 + b12]++;
                              tileRedrawTimers[i6 + b10][l9 + b12] = 24;
                           }
                        }
                     } else if (i3 >= 16) {
                        objectMap[i6][l9] = -1;
                        markAreaDirty(i6, l9);
                     }

                     objectState[i6][l9] = i3 + 1;
                     tileRedrawTimers[i6][l9] = 24;
                     break label768;
                  case 32:
                     this.updateHook();
                     break label768;
                  case 36:
                     GameCanvas gamecanvas13 = this;
                     i3 = this.scanX;
                     int l5 = gamecanvas13.scanY;
                     if (objectState[i3][l5] == 0) {
                        if (objectMap[i3][l5 - 1] == 11) {
                           objectState[i3][l5] = 1;
                           gamecanvas13.pressActiveSwitch();
                        }
                     } else if (gamecanvas13.isPlayerAt(i3, l5 - 1)) {
                        gamecanvas13.hurtPlayer(1, 64, 0);
                     }
                     break label768;
                  case 37:
                     GameCanvas gamecanvas12 = this;
                     i3 = this.scanX;
                     int k5 = gamecanvas12.scanY;
                     int k9;
                     if ((k9 = objectState[i3][k5]) > 0) {
                        if (k9 >= 8) {
                           gamecanvas12.alertCreatures(i3, k5);
                           objectMap[i3][k5] = -1;
                           markAreaDirty(i3, k5);
                        }

                        objectState[i3][k5] = k9 + 1;
                        tileRedrawTimers[i3][k5] = 24;
                     }
                     break label768;
                  case 40:
                     this.updatePickup(40);
                     break label768;
                  case 41:
                     this.updatePickup(41);
                     break label768;
                  case 42:
                     this.updatePickup(42);
                     break label768;
                  case 43:
                     this.updateRoller((byte)43);
                     break label768;
                  case 44:
                     this.updateStalactite();
                     break label768;
                  case 45:
                     GameCanvas gamecanvas11 = this;
                     i3 = this.scanX;
                     int j5 = gamecanvas11.scanY;
                     tileRedrawTimers[i3][j5] = 24;
                     int j9 = (objectState[i3][j5] & 7168) >> 10;
                     int i12 = objectState[i3][j5] & 15;
                     int k14 = 0;
                     boolean flag1 = (tileMap[i3][j5] & 0xFF) == 35;
                     if (i12 == 10) {
                        k14 = 100;
                     } else {
                        int l17 = sprites[28].getAnimFrameCount(i12);

                        for (int l19 = 0; l19 < l17; l19++) {
                           k14 += sprites[28].getAnimFrameDuration(i12, l19);
                        }
                     }

                     int i18 = (objectState[i3][j5] & 2088960) >> 13;
                     objectState[i3][j5] = objectState[i3][j5] & -2088961 | ++i18 << 13;
                     byte[] abyte2;
                     byte b27;
                     if (i12 >= 4 && i12 <= 9) {
                        abyte2 = objectTimers[i3];
                        b24 = j5;
                        b27 = 12;
                     } else {
                        abyte2 = objectTimers[i3];
                        b24 = j5;
                        b27 = 0;
                     }

                     abyte2[b24] = b27;
                     if (i18 > k14 >> 1) {
                        if (!flag1) {
                           if (gamecanvas11.isPlayerAt(i3, j5) && i12 != 10) {
                              byte b19;
                              switch (i12) {
                                 case 4:
                                 case 5:
                                    b19 = 1;
                                    break;
                                 case 6:
                                    b19 = 2;
                                    break;
                                 case 7:
                                 case 8:
                                    b19 = 3;
                                    break;
                                 case 9:
                                    b19 = 4;
                                    break;
                                 default:
                                    b19 = oppositeDirection[gamecanvas11.playerFlags & 7];
                              }

                              byte b15 = b19;
                              gamecanvas11.hurtPlayer(1, 48, b15);
                           }

                           if (isRestingOnSomething(i3, j5)) {
                              objectMap[i3][j5] = -1;
                              gamecanvas11.triggerTile(i3, j5);
                              break label768;
                           }
                        }

                        if (i18 >= k14) {
                           markAreaDirty(i3, j5);
                           i18 = gamecanvas11.directionToward(i3, j5, gamecanvas11.playerX, gamecanvas11.playerY, true);
                           int l14 = 0;
                           switch (i12) {
                              case 0:
                              case 3:
                              case 4:
                              case 7:
                              case 9:
                                 l14 = 1;
                              case 1:
                              case 2:
                              case 5:
                              case 6:
                              case 8:
                              default:
                                 boolean flag2 = i18 == 4;
                                 int l2;
                                 int l20;
                                 if (l14 != (flag2 ? 1 : 0)) {
                                    l20 = flag2 ? 3 : 2;
                                    l14 = i3;
                                    l2 = j5;
                                 } else {
                                    label1229: {
                                       l14 = i3 - directionOffsets[i18];
                                       l2 = j5 - directionOffsets[i18 + 8];
                                       byte b20;
                                       if (flag2) {
                                          if (gamecanvas11.isPlayerAt(l14, l2) && i12 != 0) {
                                             l20 = 0;
                                             l14 = i3;
                                             l2 = j5;
                                             break label1229;
                                          }

                                          switch (i18) {
                                             case 1:
                                                b20 = 4;
                                                break;
                                             case 2:
                                             default:
                                                b20 = 0;
                                                break;
                                             case 3:
                                                b20 = 7;
                                                break;
                                             case 4:
                                                b20 = 9;
                                          }
                                       } else {
                                          if (gamecanvas11.isPlayerAt(l14, l2) && i12 != 1) {
                                             l20 = 1;
                                             l14 = i3;
                                             l2 = j5;
                                             break label1229;
                                          }

                                          switch (i18) {
                                             case 1:
                                                b20 = 5;
                                                break;
                                             case 2:
                                                b20 = 6;
                                                break;
                                             case 3:
                                                b20 = 8;
                                                break;
                                             default:
                                                b20 = 1;
                                          }
                                       }

                                       l20 = b20;
                                    }
                                 }

                                 if (objectMap[l14][l2] >= 0 || flag1) {
                                    if (l14 != i3 || l2 != j5) {
                                       l20 = 0;
                                    }

                                    l14 = i3;
                                    l2 = j5;
                                 }

                                 objectMap[i3][j5] = -1;
                                 objectTimers[l14][l2] = 0;
                                 objectState[l14][l2] = j9 << 10 | l20;
                                 objectState[l14][l2] = objectState[l14][l2] & -2088961;
                                 objectMap[l14][l2] = 45;
                           }
                        }
                     }

                     if (flag1) {
                        if (!gamecanvas11.isPlayerAt(i3, j5) || i12 == 10) {
                           break label768;
                        }
                     } else {
                        int i21 = i3;
                        i18 = j5;
                        switch (i12) {
                           case 0:
                           case 1:
                           case 2:
                           case 3:
                           default:
                              break;
                           case 4:
                           case 5:
                              i18++;
                              break;
                           case 6:
                              i21--;
                              break;
                           case 7:
                           case 8:
                              i18--;
                              break;
                           case 9:
                              i21++;
                              break;
                           case 10:
                              i21 = -1;
                              i18 = -1;
                        }

                        if (!gamecanvas11.isPlayerAt(i21, i18)) {
                           break label768;
                        }
                     }

                     gamecanvas11.hurtPlayer(1, 48, oppositeDirection[gamecanvas11.playerFlags & 7]);
                     break label768;
                  case 46:
                     GameCanvas gamecanvas10 = this;
                     i3 = this.scanX;
                     int i5 = gamecanvas10.scanY;
                     int i9 = objectState[i3][i5] & 31;
                     int l11 = (objectState[i3][i5] & 8160) >> 5;
                     int j14 = animLength(sprites[29], i9);
                     if ((tileMap[i3][i5] & 0xFF) == 35) {
                        if (++l11 > j14) {
                           l11 = 0;
                        }

                        objectState[i3][i5] = 0 | l11 << 5;
                        break label768;
                     }

                     if (objectMap[i3][i5 + 1] < 0 && i9 != 8 && i9 != 9) {
                        byte b18;
                        switch (i9) {
                           case 0:
                           case 2:
                           case 4:
                           case 6:
                              b18 = 8;
                              break;
                           case 1:
                           case 3:
                           case 5:
                           default:
                              b18 = 9;
                        }

                        byte b5 = b18;
                        objectTimers[i3][i5 + 1] = 18;
                        objectMap[i3][i5 + 1] = 46;
                        objectMap[i3][i5] = -1;
                        objectState[i3][i5 + 1] = b5;
                        markAreaDirty(i3, i5);
                        break label768;
                     }

                     if (i9 != 8 && i9 != 9) {
                        if (isRestingOnSomething(i3, i5)) {
                           objectMap[i3][i5] = -1;
                           gamecanvas10.triggerTile(i3, i5);
                           markAreaDirty(i3, i5);
                           break label768;
                        }

                        if (!gamecanvas10.isPlayerAt(i3 - 1, i5) && !gamecanvas10.isPlayerAt(i3 + 1, i5) && !gamecanvas10.isPlayerAt(i3, i5 - 1)) {
                           int k17 = gamecanvas10.playerFlags & 7;
                           label864:
                           if (gamecanvas10.playerX == i3
                              && gamecanvas10.moveProgress == 6
                              && (k17 == 4 || k17 == 2)
                              && gamecanvas10.playerY < i5
                              && objectMap[i3][i5 - 1] < 0
                              && i5 * 24 <= gamecanvas10.cameraY + 240 - 62) {
                              byte b17;
                              switch (i9) {
                                 case 0:
                                 case 2:
                                    b17 = 6;
                                    break;
                                 case 1:
                                 case 3:
                                    b17 = 7;
                                    break;
                                 default:
                                    break label864;
                              }

                              i9 = b17;
                              l11 = 0;
                           } else if (gamecanvas10.playerY != i5
                              || gamecanvas10.moveProgress != 6
                              || k17 != 1 && k17 != 3
                              || i9 < 0
                              || i9 > 3
                              || (gamecanvas10.playerX >= i3 || objectMap[i3 - 1][i5] >= 0 || i3 * 24 >= gamecanvas10.cameraX + 320)
                                 && (gamecanvas10.playerX <= i3 || objectMap[i3 + 1][i5] >= 0 || (i3 + 1) * 24 <= gamecanvas10.cameraX)) {
                              label828: {
                                 l11++;
                                 byte[] abyte1;
                                 switch (i9) {
                                    case 4:
                                       int k19 = i3 - 1;
                                       if (objectMap[k19][i5] >= 0 || l11 != firstFrameDuration(sprites[29], 4, 1)) {
                                          break label828;
                                       }

                                       objectMap[k19][i5] = 21;
                                       objectState[k19][i5] = 4;
                                       objectTimers[k19][i5] = 18;
                                       abyte1 = tileRedrawTimers[k19];
                                       b24 = i5;
                                       break;
                                    case 5:
                                       int k20 = i3 + 1;
                                       if (objectMap[k20][i5] >= 0 || l11 != firstFrameDuration(sprites[29], 5, 1)) {
                                          break label828;
                                       }

                                       objectMap[k20][i5] = 21;
                                       objectState[k20][i5] = 2;
                                       objectTimers[k20][i5] = 18;
                                       abyte1 = tileRedrawTimers[k20];
                                       b24 = i5;
                                       break;
                                    case 6:
                                    case 7:
                                       k17 = i5 - 1;
                                       if (objectMap[i3][k17] < 0 && l11 == firstFrameDuration(sprites[29], i9, 1)) {
                                          objectMap[i3][k17] = 21;
                                          objectState[i3][k17] = 1;
                                          objectTimers[i3][k17] = 18;
                                          abyte1 = tileRedrawTimers[i3];
                                          b24 = k17;
                                          break;
                                       }
                                    default:
                                       break label828;
                                 }

                                 abyte1[b24] = 24;
                              }

                              label812:
                              if (l11 > j14) {
                                 l11 = 0;
                                 int l21;
                                 switch (gamecanvas10.directionToward(i3, i5, gamecanvas10.playerX, gamecanvas10.playerY, false)) {
                                    case 1:
                                       if (gamecanvas10.playerX == i3 && i9 != 6 && i9 != 7 && i5 * 24 <= gamecanvas10.cameraY + 240 - 62) {
                                          i9 = i9 == 2 ? 6 : 7;
                                       } else {
                                          i9 = gamecanvas10.playerX < i3 ? 2 : 3;
                                       }
                                       break label812;
                                    case 2:
                                       l21 = gamecanvas10.playerY == i5 && i9 != 5 && (i3 + 1) * 24 > gamecanvas10.cameraX ? 5 : 1;
                                       break;
                                    case 3:
                                    default:
                                       l21 = gamecanvas10.playerX < i3 ? 0 : 1;
                                       break;
                                    case 4:
                                       l21 = gamecanvas10.playerY == i5 && i9 != 4 && i3 * 24 < gamecanvas10.cameraX + 320 ? 4 : 0;
                                 }

                                 i9 = l21;
                              }
                           } else {
                              i9 = gamecanvas10.playerX < i3 ? 4 : 5;
                              l11 = 0;
                           }
                        } else {
                           label871: {
                              byte b16;
                              if (gamecanvas10.playerY == i5 - 1) {
                                 b16 = 17;
                              } else if (gamecanvas10.playerX == i3 - 1) {
                                 b16 = 16;
                              } else {
                                 if (gamecanvas10.playerX != i3 + 1) {
                                    break label871;
                                 }

                                 b16 = 15;
                              }

                              i9 = b16;
                           }

                           l11 = 0;
                        }

                        tileRedrawTimers[i3][i5] = 24;
                        objectState[i3][i5] = i9 & 31 | l11 << 5;
                        break label768;
                     }

                     objectTimers[i3][i5] = (byte)(objectTimers[i3][i5] - 6);
                     if (objectTimers[i3][i5] < 0) {
                        if (objectMap[i3][i5 + 1] < 0) {
                           objectTimers[i3][i5 + 1] = 18;
                           objectMap[i3][i5 + 1] = 46;
                           objectMap[i3][i5] = -1;
                           objectState[i3][i5 + 1] = i9;
                           markAreaDirty(i3, i5);
                        } else {
                           i9 = i9 == 8 ? 10 : 11;
                           objectState[i3][i5] = i9;
                           objectTimers[i3][i5] = 0;
                        }
                     } else if (actorsTouch(
                        i3, i5, 3, objectTimers[i3][i5], gamecanvas10.playerX, gamecanvas10.playerY, gamecanvas10.playerFlags & 7, gamecanvas10.moveProgress
                     )) {
                        gamecanvas10.hurtPlayer(1, 48, objectState[i3][i5] & 7);
                     }
                     break label768;
                  case 47:
                     this.updateObjectAtScan();
                     this.spawnWindPod();
                     break label768;
                  case 48:
                     if ((objectState[this.scanX][this.scanY] & 8) == 0) {
                        this.updateFallingBlock((byte)48);
                     } else {
                        this.trackCollapsingRow();
                     }
                     break label768;
                  case 49:
                     this.updateRoller((byte)49);
                     break label768;
                  case 50:
                     if (this.moveProgress < 12 && this.isPlayerAt(this.scanX, this.scanY) && !this.bossSlamHitPlayer) {
                        this.hurtPlayer(1, 48, oppositeDirection[this.playerFlags & 7]);
                     }
                     break label768;
                  case 51:
                     this.updatePickup(51);
                     break label768;
                  case 52:
                     this.updatePickup(52);
                     break label768;
                  case 53:
                     this.updatePickup(53);
                     break label768;
                  case 54:
                     GameCanvas gamecanvas9 = this;
                     i3 = this.scanX;
                     int l4 = gamecanvas9.scanY;
                     int l8 = objectState[i3][l4];
                     Sprite sprite = sprites[7];
                     l8++;
                     int i14 = animLength(sprite, 0);
                     if (l8 >= i14) {
                        objectMap[i3][l4] = -1;
                        markAreaDirty(i3, l4);
                        break label768;
                     }

                     if (l8 == 1) {
                        gamecanvas9.playSound(7);
                        markAreaDirty(i3, l4);
                     } else if (l8 == i14 >> 1) {
                        for (int k16 = -1; k16 < 2; k16++) {
                           for (int j17 = -1; j17 < 2; j17++) {
                              int k2;
                              int j19;
                              label781: {
                                 j19 = i3 + j17;
                                 k2 = l4 + k16;
                                 int[] aint5;
                                 byte b26;
                                 switch (objectMap[j19][k2]) {
                                    case 8:
                                       objectMap[j19][k2] = 54;
                                       aint5 = objectState[j19];
                                       b24 = k2;
                                       b26 = 0;
                                       break;
                                    case 10:
                                       if (gamecanvas9.enemyPhase != 3) {
                                          break label781;
                                       }
                                    case 30:
                                    case 37:
                                       aint5 = objectState[j19];
                                       b24 = k2;
                                       b26 = 1;
                                       break;
                                    case 16:
                                    case 19:
                                    case 43:
                                    case 49:
                                       objectMap[j19][k2] = -1;
                                       gamecanvas9.triggerTile(j19, k2);
                                       tileRedrawTimers[j19][k2] = 24;
                                    default:
                                       break label781;
                                 }

                                 aint5[b24] = b26;
                                 markAreaDirty(j19, k2);
                              }

                              if (gamecanvas9.isPlayerAt(j19, k2)) {
                                 gamecanvas9.hurtPlayer(1, 64, 0);
                              }
                           }
                        }
                     }

                     objectState[i3][l4] = l8;
                     tileRedrawTimers[i3][l4] = 24;
                     break label768;
               }

               byte b4 = b0;
               byte b3 = (byte)b24;
               GameCanvas gamecanvas16 = gamecanvas22;
               int j10 = gamecanvas22.scanX;
               int k12 = gamecanvas16.scanY;
               int j15 = b3 == 23 ? -1 : 1;
               tileRedrawTimers[j10][k12] = 24;
               if (gamecanvas16.playerY == k12) {
                  for (int l16 = 0; l16 <= b4; l16++) {
                     int j18 = j10 + l16 * j15;
                     if (gamecanvas16.playerX == j18) {
                        gamecanvas16.hurtPlayer(1, 64, 0);
                     }
                  }
               }
            }

            gamecanvas22 = this;
            k22 = this.scanX + 1;
         }
      }
   }

   private void trackCollapsingRow() {
      int i;
      int i1;
      int k;
      label21: {
         i = this.scanX;
         int j = (k = this.scanY) + 1;
         switch ((j = objectState[i][j]) & 7) {
            case 2:
               i1 = i;
               break;
            case 4:
               i1 = i + 1;
               break label21;
            default:
               if ((j & 16) == 0) {
                  i1 = i + 1;
                  break label21;
               }

               i1 = i;
         }

         i1 -= 1;
      }

      int l = i1;
      i = (objectState[i][k] >> 24) * 3;
      byte[] abyte;
      int j1;
      byte b0;
      if (objectMap[l][k] < 0) {
         collapsingRows[i + 2] = (byte)k;
         collapsingRows[i + 1] = (byte)l;
         abyte = collapsingRows;
         j1 = i;
         b0 = (byte)l;
      } else {
         abyte = collapsingRows;
         j1 = i + 2;
         b0 = -1;
      }

      abyte[j1] = b0;
   }

   private void updateFallingBlock(byte var1) {
      int x = this.scanX;   // the byte parameter is unused; its slot held scanX
      int i = this.scanY;
      if (objectTimers[x][i] == 6 && (tileMap[x][i] & 0xFF) == 6) {
         this.activateGate(tileMap[x][i] >> 8);
      }

      if (objectTimers[x][i] <= 0) {
         int j;
         label40: {
            int k;
            int i1;
            if ((k = (j = objectState[x][i]) & 7) == 2) {
               i1 = j | 16;
            } else {
               if (k != 4) {
                  break label40;
               }

               i1 = j & -17;
            }

            j = i1;
         }

         int l = i + 1;
         if (objectMap[x][l] >= 0 || this.isPlayerAt(x, l) && this.deathTimer == 0) {
            if ((j & 7) == 3 && this.isPlayerAt(x, l)) {
               this.hurtPlayer(2, 48, 0);
            }

            objectState[x][i] = j & -8;
         } else {
            objectMap[x][i - 1] = -1;
            objectMap[x][l] = 48;
            objectState[x][l] = j & -8 | 3;
            objectState[x][i] = objectState[x][i - 1] | 8;
            objectTimers[x][l] = 18;
            j = i - 2;
            tileRedrawTimers[x - 1][j] = 48;
            tileRedrawTimers[x][j] = 48;
            tileRedrawTimers[x + 1][j] = 48;
            markAreaDirty(x, i);
            this.trackCollapsingRow();
         }
      } else {
         objectTimers[x][i] = (byte)(objectTimers[x][i] - 6);
      }

      tileRedrawTimers[x][i] = 24;
      tileRedrawTimers[x][i - 1] = 24;
   }

   private static boolean canHoldObject(int var0, int var1) {
      byte b0 = objectMap[var0][var1];
      var0 = tileMap[var0][var1] & 0xFF;
      return b0 < 80 && b0 != 30 && b0 != 10 && b0 != 37 && b0 != 34 && b0 != 35 && var0 != 14 && var0 != 33 && var0 != 15 && var0 != 4 && var0 != 16;
   }

   private static boolean isEmptyFloor(int var0, int var1) {
      byte b0 = objectMap[var0][var1];
      var0 = tileMap[var0][var1] & 0xFF;
      return b0 == -1 && var0 != 14 && var0 != 33 && var0 != 5 && var0 != 28;
   }

   private static boolean isFreeTile(int var0, int var1) {
      byte b0 = objectMap[var0][var1];
      int i = tileMap[var0][var1] & 0xFF;
      return b0 == -1 && i != 14 && i != 33 && i != 4 && i != 32 && (i != 7 || (tileMap[var0][var1] >> 8 & 240) != 0);
   }

   private void spawnWindPod() {
      int i = this.scanY - 1;
      boolean flag = tileSlots != null && tileSlots[this.scanX][this.scanY] != 0 && tileSlots[this.scanX][this.scanY - 1] == 0;
      if ((tileMap[this.scanX][i] & 0xFF) != 35
         && canHoldObject(this.scanX, i)
         && (!this.isPlayerAt(this.scanX - 1, this.scanY) && !this.isPlayerAt(this.scanX + 1, this.scanY) || (this.playerFlags & 8) == 0)
         && (objectMap[this.scanX][this.scanY + 1] >= 0 || flag)) {
         System.out.println("In ProcessWindPod condition throw");
         tileMap[this.scanX][i] = 35;
         objectTimers[this.scanX][i] = 18;
         tileRedrawTimers[this.scanX][i] = 24;
      }
   }

   private static int animLength(Sprite var0, int var1) {
      int i = 0;
      int j = var0.animFrameCounts[var1] & 255;

      for (int k = 0; k < j; k++) {
         i += var0.animFrames[(var0.animFrameStarts[var1] + k) * 5 + 1] & 255;
      }

      return i;
   }

   private void updateStalactite() {
      int i = this.scanX;
      int j = this.scanY;
      tileRedrawTimers[i][j] = 24;
      byte[] abyte;
      int l;
      byte b1;
      switch ((objectState[i][j] & 56) >> 3) {
         case 0:
            if ((this.playerX != i || (j + 1) * 24 <= this.cameraY || this.stageKind == 3)
               && (this.stageKind != 3 || this.icicleDropTime == 0L || stateTicks < this.icicleDropTime + (21 - i))) {
               return;
            }

            int k = j + 1;

            while (true) {
               byte b0 = objectMap[i][k];
               if (this.playerY == k || b0 >= 80 || b0 == 30 || b0 == 34 || b0 == 35 || b0 == 0) {
                  if (this.playerY == k || this.stageKind == 3) {
                     objectState[i][j] = objectState[i][j] & -57 | 8;
                     objectTimers[i][j] = 10;
                     return;
                  }

                  return;
               }

               k++;
            }
         case 1:
            objectTimers[i][j]--;
            if (objectTimers[i][j] <= 0) {
               tileMap[i][j] = 34;
               objectState[i][j] = objectState[i][j] & -64 | 24 | 3;
               objectTimers[i][j] = 0;
               return;
            }

            return;
         case 2:
         default:
            return;
         case 3:
            if (objectTimers[i][j] > 0) {
               objectTimers[i][j] = (byte)(objectTimers[i][j] - 5);
               return;
            }

            boolean flag = this.isPlayerAt(i, j + 1);
            boolean flag1 = false;
            if (flag || objectMap[i][j + 1] >= 0 || this.hasEnemies && tileSlots[i][j + 1] != 0) {
               if (flag) {
                  this.hurtPlayer(1, 48, 0);
                  flag1 = true;
               } else {
                  flag1 = true;
                  switch (objectMap[i][j + 1]) {
                     case 10:
                        tileMap[i][j + 1] = 32;
                        this.refreshBackgroundTile(i, j + 1);
                        flag1 = false;
                        break;
                     case 18:
                        this.tryStartFloorToggle();
                        break;
                     case 19:
                     case 43:
                     case 45:
                     case 46:
                     case 49:
                        this.triggerTile(i, j + 1);
                        objectMap[i][j + 1] = -1;
                        break;
                     case 21:
                        flag1 = false;
                        break;
                     case 30:
                        this.playSound(11);
                        objectState[i][j + 1] = 1;
                        break;
                     default:
                        this.playSound(14);
                  }
               }
            }

            if (flag1) {
               objectState[i][j] = objectState[i][j] & -64 | 32;
               objectTimers[i][j] = 0;
               return;
            }

            objectMap[i][j] = -1;
            objectMap[i][j + 1] = 44;
            objectState[i][j + 1] = objectState[i][j];
            abyte = objectTimers[i];
            l = j + 1;
            b1 = 19;
            break;
         case 4:
            if ((stateTicks & 1) != 0) {
               return;
            }

            objectTimers[i][j]++;
            if (objectTimers[i][j] != sprites[27].getAnimFrameCount(4)) {
               return;
            }

            abyte = objectMap[i];
            l = j;
            b1 = -1;
      }

      abyte[l] = b1;
      markAreaDirty(i, j);
   }

   private void updatePickup(int var1) {
      int i = this.scanX;
      int j = this.scanY;
      if (this.moveProgress <= 0 && this.isPlayerAt(i, j)) {
         int k;
         if ((k = tileMap[i][j] & 0xFF) != 14 && k != 33 || tileMap[i][j] >> 8 != 255) {
            label37: {
               this.pickupObject = var1;
               GameCanvas gamecanvas;
               byte b0;
               switch (var1) {
                  case 2:
                     this.pickupIconSet = 3;
                     gamecanvas = this;
                     b0 = 0;
                     break;
                  case 4:
                     this.pickupIconSet = 24;
                     gamecanvas = this;
                     b0 = 0;
                     break;
                  case 5:
                     this.pickupIconSet = 25;
                     gamecanvas = this;
                     b0 = 0;
                     break;
                  case 6:
                     if (this.lives >= 99) {
                        objectMap[i][j] = 7;
                        objectState[i][j] = 0;
                        this.updatePickup(7);
                        break label37;
                     }

                     this.pickupIconSet = 5;
                     gamecanvas = this;
                     b0 = 0;
                     break;
                  case 7:
                     if (this.energy == saveData[8]) {
                        objectMap[i][j] = 41;
                        objectState[i][j] = 10;
                        this.stageDiamondTotal += 10;
                        this.updatePickup(41);
                        break label37;
                     }

                     this.pickupIconSet = 5;
                     gamecanvas = this;
                     b0 = 1;
                     break;
                  case 40:
                     this.pickupIconSet = 19;
                     this.pickupIconFrame = 0;
                     objectMap[this.scanX][this.scanY] = -1;
                     this.pickupObject = 40;
                     saveData[10] = 1;
                     break label37;
                  case 41:
                     this.pickupIconSet = 2;
                     this.pickupIconFrame = 0;
                     this.pickupValue = objectState[this.scanX][this.scanY];
                     break label37;
                  case 42:
                     this.pickupIconSet = 29;
                     gamecanvas = this;
                     b0 = 0;
                     break;
                  case 51:
                     this.pickupIconSet = 30;
                     gamecanvas = this;
                     b0 = 0;
                     break;
                  case 52:
                     this.pickupIconSet = 31;
                     gamecanvas = this;
                     b0 = 0;
                     break;
                  case 53:
                     this.pickupIconSet = 32;
                     gamecanvas = this;
                     b0 = 0;
                     break;
                  default:
                     break label37;
               }

               gamecanvas.pickupIconFrame = b0;
            }

            objectMap[i][j] = -1;
            this.hudDirty = true;
         }
      }
   }

   private boolean isTrapBlocking(int var1, int var2) {
      return objectMap[var1][var2] == 28
         || this.trapLengthA >= 24
            && (
               objectMap[var1][var2 - 1] == 28 && (objectState[var1][var2 - 1] & 8) == 0
                  || objectMap[var1][var2 + 1] == 28 && (objectState[var1][var2 + 1] & 8) == 0
            )
         || this.trapLengthB >= 24 && (objectMap[var1][var2 - 1] == 28 || objectMap[var1][var2 + 1] == 28);
   }

   private int directionToward(int var1, int var2, int var3, int var4, boolean var5) {
      var3 = var1 - var3;
      var4 = var2 - var4;
      int i = var3 > 0 ? var3 : -var3;
      int j = var4 > 0 ? var4 : -var4;
      byte b0 = 0;
      if (i > j) {
         label79: {
            byte b3;
            if (var3 > 0) {
               b3 = 4;
            } else {
               if (var3 >= 0) {
                  break label79;
               }

               b3 = 2;
            }

            b0 = b3;
         }

         if (b0 != 0 && (!isFreeTile(var1 - directionOffsets[b0], var2) || this.isTrapBlocking(var1 - directionOffsets[b0], var2))) {
            b0 = 0;
         }
      }

      if (b0 == 0) {
         label66: {
            byte b1;
            if (var4 > 0) {
               b1 = 1;
            } else {
               if (var4 >= 0) {
                  break label66;
               }

               b1 = 3;
            }

            b0 = b1;
         }

         var4 = var2 - directionOffsets[b0 + 8];
         if (var5 && b0 != 0 && (!isFreeTile(var1, var4) || this.hasEnemies && tileSlots[var1][var4] != 0 || this.isTrapBlocking(var1, var4))) {
            label50: {
               b0 = 0;
               byte b2;
               if (var3 > 0) {
                  b2 = 4;
               } else {
                  if (var3 >= 0) {
                     break label50;
                  }

                  b2 = 2;
               }

               b0 = b2;
            }

            if (b0 != 0 && objectMap[var1 - directionOffsets[b0]][var2] >= 0) {
               b0 = 0;
            }
         }
      }

      return b0;
   }

   private void updateRoller(byte var1) {
      int i = this.scanX;
      int j = this.scanY;
      int k = objectState[i][j];
      byte b0 = objectTimers[i][j];
      int l = 0;
      int i1 = 0;
      int j1 = (tileMap[i][j] & 0xFF) == 35 ? 1 : 0;
      boolean flag = var1 == 43 && (k & 3840) != 0;
      if (j1 == 0 && isRestingOnSomething(i, j)) {
         objectMap[i][j] = -1;
         this.triggerTile(i, j);
      } else {
         int k1;
         label121: {
            k1 = k & 7;
            byte[] abyte;
            int i2;
            byte b1;
            if (b0 <= 0) {
               if (j1 == 0 || b0 > 6) {
                  markAreaDirty(i, j);
                  if (flag) {
                     b0 = 18;
                     j1 = this.directionToward(i, j, this.playerX, this.playerY, true);
                     k = k & -8 | j1;
                     k1 = j1;
                     l = -directionOffsets[j1];
                     i1 = -directionOffsets[j1 + 8];
                     if (j1 == 0) {
                        b0 = 0;
                        i1 = 0;
                        l = 0;
                     }

                     k -= 256;
                  } else {
                     label146: {
                        if ((k & 16646144) != 0 && (k & 248) == 0) {
                           j1 = (k & 16646144) >> 17;
                           k1 = (k & 2130706432) >> 24;
                           if (i == j1 && j == k1) {
                              int l1;
                              k1 = ((l1 = k & -16646145) & -2147483648) == 0 ? 2 : 1;
                              k = l1 & -8 | k1;
                              break label146;
                           }

                           b0 = 21;
                           j1 = this.directionToward(i, j, j1, k1, true);
                           k = k & -8 | j1;
                           l = -directionOffsets[j1];
                           i1 = -directionOffsets[j1 + 8];
                           k1 = j1;
                           if (j1 != 0) {
                              break label146;
                           }

                           b0 = 0;
                        } else {
                           if (k1 == 0) {
                              b0 = 21;
                              j1 = (k & 28672) >> 12;
                              k = k & -8 | j1;
                              k1 = j1;
                              l = -directionOffsets[k1];
                              i1 = -directionOffsets[k1 + 8];
                              if (!isFreeTile(i + l, j + i1)) {
                                 i1 = 0;
                                 l = 0;
                                 b0 = 0;
                              }
                              break label146;
                           }

                           b0 = 21;
                           l = -directionOffsets[k1];
                           i1 = -directionOffsets[k1 + 8];
                           if (isFreeTile(i + l, j + i1)) {
                              break label146;
                           }

                           label106: {
                              int j2;
                              short short1;
                              switch (k1) {
                                 case 1:
                                    j2 = k & -28673;
                                    short1 = 12288;
                                    break;
                                 case 2:
                                    j2 = k & -28673;
                                    short1 = 16384;
                                    break;
                                 case 3:
                                    j2 = k & -28673;
                                    short1 = 4096;
                                    break;
                                 case 4:
                                    j2 = k & -28673;
                                    short1 = 8192;
                                    break;
                                 default:
                                    break label106;
                              }

                              k = j2 | short1;
                           }

                           k &= -8;
                           k1 = 0;
                        }

                        i1 = 0;
                        l = 0;
                     }
                  }
               } else if (b0 < 0) {
                  objectTimers[i][j] = 0;
               }

               if ((k & 248) == 0) {
                  objectMap[i][j] = -1;
                  objectMap[i + l][j + i1] = var1;
                  tileRedrawTimers[i + l][j + i1] = 48;
                  objectTimers[i + l][j + i1] = b0;
                  objectState[i + l][j + i1] = k;
                  break label121;
               }

               if ((stateTicks & 3) == 0) {
                  k = k & -249 | (k & 248) - 8;
                  if (var1 == 43 && (k & 248) == 0) {
                     k = k & -3841 | 3072;
                  }
               }

               abyte = objectTimers[i];
               i2 = j;
               b1 = 0;
            } else {
               if (b0 < 0) {
                  objectTimers[i][j] = 0;
               }

               b0 = (byte)(b0 - 3);
               abyte = objectTimers[i];
               i2 = j;
               b1 = b0;
            }

            abyte[i2] = b1;
         }

         if ((k & 248) == 0
            && (spriteInstances[0].anim < 13 || spriteInstances[0].anim > 16)
            && actorsTouch(i, j, k1, objectTimers[i][j], this.playerX, this.playerY, (this.playerFlags & 4096) == 0 ? this.queuedAction : 0, this.moveProgress)
            )
          {
            this.hurtPlayer(1, 48, k1);
            if (flag) {
               k &= -3841;
            }
         }

         objectState[i][j] = k;
      }
   }

   private void pressActiveSwitch() {
      if (this.stageKind != 3 && this.stageKind != 4 && this.stageKind != 5 || this.bossHealth <= 0) {
         if (activeSwitchId >= 0) {
            switchStates[activeSwitchId]--;
            if (switchStates[activeSwitchId] == 0) {
               int i = activeSwitchId;
               GameCanvas gamecanvas = this;
               this.playSound(8);
               int j = gamecanvas.mapWidth - 1;
               int l1 = gamecanvas.mapHeight - 1;

               for (int k = 1; k < l1; k++) {
                  for (int l = 1; l < j; l++) {
                     if ((tileMap[l][k] & 0xFF) == 17 && tileMap[l][k] >> 8 == i) {
                        int i1;
                        switch (i1 = tileMap[l][k - 1] & 0xFF) {
                           case 7:
                              int j1 = k - 1;
                              i1 = l;
                              int k1;
                              if (((k1 = tileMap[i1][j1] >> 8) & 240) == 0) {
                                 k1 |= 16;
                                 tileMap[i1][j1] = k1 << 8 | 7;
                                 tileRedrawTimers[i1][j1] = 24;
                              }
                              break;
                           case 14:
                           case 33:
                              tileMap[l][k - 1] = 0 | i1;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void collectTreasure(int var1) {
      if ((tileMap[this.scanX][this.scanY] & 0xFF) != 14 || tileMap[this.scanX][this.scanY] >> 8 != 255) {
         if (this.isPlayerAt(this.scanX, this.scanY) && (spriteInstances[0].anim == 40 || spriteInstances[0].anim == 48)) {
            label22: {
               objectMap[this.scanX][this.scanY] = -1;
               GameCanvas gamecanvas;
               byte b0;
               switch (var1) {
                  case 24:
                     saveData[9] = 1;
                     this.pickupIconSet = 7;
                     this.pickupIconFrame = 0;
                     gamecanvas = this;
                     b0 = 24;
                     break;
                  case 25:
                  default:
                     break label22;
                  case 26:
                     saveData[9] = 8;
                     this.pickupIconSet = 7;
                     this.pickupIconFrame = 2;
                     gamecanvas = this;
                     b0 = 26;
                     break;
                  case 27:
                     saveData[9] = 2;
                     this.pickupIconSet = 7;
                     this.pickupIconFrame = 1;
                     gamecanvas = this;
                     b0 = 27;
               }

               gamecanvas.pickupObject = b0;
            }

            this.markTreasureTaken(this.world, this.stage, this.scanX, this.scanY);
         }
      }
   }

   private void updateObject11() {
      int i = this.scanX;
      int j = this.scanY;
      int k;
      if ((k = (objectState[i][j] & 3840) >> 8) != 0) {
         if (k >= 4) {
            objectMap[i][j] = -1;
         } else if ((stateTicks >> 1 & 1) == 0) {
            objectState[i][j] = objectState[i][j] + 256;
         }
      } else if (tileSlots != null && tileSlots[i][j] != 0) {
         objectState[i][j] = objectState[i][j] & -3841 | 256;
      } else if (objectTimers[i][j] <= 4) {
         label162: {
            k = objectState[i][j];
            tileRedrawTimers[i][j] = 24;
            boolean flag = (k & 16) != 0;
            int l;
            int[] aint;
            int i1;
            int j1;
            int k1;
            if ((l = k & 7) != 0) {
               byte b0;
               byte b1;
               byte b2;
               byte b3;
               byte b4;
               byte b5;
               label153: {
                  b0 = 0;
                  b1 = 0;
                  b2 = 0;
                  b3 = 0;
                  b4 = 0;
                  b5 = 0;
                  byte b6;
                  switch (l) {
                     case 1:
                        b0 = (byte)(flag ? -1 : 1);
                        b4 = (byte)(flag ? 4 : 2);
                        b5 = (byte)(flag ? 2 : 4);
                        b3 = -1;
                        break label153;
                     case 2:
                        b1 = (byte)(flag ? -1 : 1);
                        b4 = (byte)(flag ? 1 : 3);
                        b5 = (byte)(flag ? 3 : 1);
                        b6 = 1;
                        break;
                     case 3:
                        b0 = (byte)(flag ? 1 : -1);
                        b4 = (byte)(flag ? 2 : 4);
                        b5 = (byte)(flag ? 4 : 2);
                        b3 = 1;
                        break label153;
                     case 4:
                        b1 = (byte)(flag ? 1 : -1);
                        b4 = (byte)(flag ? 3 : 1);
                        b5 = (byte)(flag ? 1 : 3);
                        b6 = -1;
                        break;
                     default:
                        break label153;
                  }

                  b2 = b6;
               }

               if (isFreeTile(i + b2, j + b3) && isFreeTile(i + b0, j + b1) && isFreeTile(i + b0 - b2, j + b1 - b3)) {
                  if (objectTimers[i][j] <= 0) {
                     objectState[i + b2][j + b3] = k;
                     objectMap[i + b2][j + b3] = 11;
                     objectMap[i][j] = -1;
                     objectTimers[i + b2][j + b3] = 18;
                  }
                  break label162;
               }

               if (isFreeTile(i + b0, j + b1)) {
                  objectState[i + b0][j + b1] = k & -8 | b4;
                  objectMap[i + b0][j + b1] = 11;
                  objectMap[i][j] = -1;
                  objectTimers[i + b0][j + b1] = 18;
                  break label162;
               }

               if (isFreeTile(i + b2, j + b3)) {
                  if (objectTimers[i][j] <= 0) {
                     objectState[i + b2][j + b3] = k;
                     objectMap[i + b2][j + b3] = 11;
                     objectMap[i][j] = -1;
                     objectTimers[i + b2][j + b3] = 18;
                  }
                  break label162;
               }

               aint = objectState[i];
               i1 = j;
               j1 = k & -8;
               k1 = b5;
            } else {
               label159: {
                  if (objectMap[i - 1][j] >= 0) {
                     aint = objectState[i];
                     i1 = j;
                     j1 = k & -8 | (flag ? 1 : 3);
                  } else {
                     if (objectMap[i][j + 1] < 0) {
                        break label159;
                     }

                     aint = objectState[i];
                     i1 = j;
                     j1 = k & -8 | (flag ? 2 : 4);
                  }

                  aint[i1] = j1;
               }

               if (objectMap[i + 1][j] >= 0) {
                  objectState[i][j] = k & -8 | (flag ? 3 : 1);
               }

               if (objectMap[i][j + 1] < 0) {
                  break label162;
               }

               aint = objectState[i];
               i1 = j;
               j1 = k & -8;
               k1 = flag ? 4 : 2;
            }

            aint[i1] = j1 | k1;
         }

         markAreaDirty(i, j);
      }

      if (this.isPlayerAt(i, j)) {
         this.hurtPlayer(1, 64, 0);
      }

      if (objectTimers[i][j] > 0) {
         objectTimers[i][j] = (byte)(objectTimers[i][j] - 5);
      }
   }

   private void updateHook() {
      int i = this.scanX;
      int j = this.scanY;
      tileRedrawTimers[i][j] = 24;
      byte[] abyte;
      int l1;
      byte b1;
      if (objectTimers[i][j] == 0) {
         int l;
         byte b0;
         boolean flag;
         int k;
         l = ((k = objectState[i][j]) & 1) == 0 ? -1 : 1;
         b0 = objectMap[i + l][j];
         int i1 = tileMap[i + l][j] & 0xFF;
         flag = false;
         int j1;
         label63:
         if ((j1 = k >> 1) <= 0) {
            flag = true;
         } else if (b0 < 0 && i1 != 14 && i1 != 33) {
            objectState[i + l][j] = j1 - 1 << 1 | k & 1;
            tileRedrawTimers[i + l][j] = 30;
            objectTimers[i + l][j] = 18;
            b0 = 32;
         } else if (b0 != 32) {
            k = objectState[i + l][j];
            i1 = 0;
            if (b0 != 48 || (k & 8) == 0) {
               switch (b0) {
                  case 1:
                  case 2:
                  case 4:
                  case 5:
                  case 6:
                  case 7:
                     i1 = -l;
                  case 0:
                  case 8:
                  case 9:
                  case 11:
                  case 14:
                  case 19:
                  case 43:
                  case 47:
                  case 48:
                  case 49:
                     this.playSound(12);
                     this.hookPullDelay = 0;
                     this.hookPullSteps = this.playerX - (i + l) + i1;
                     this.hookX = i + l;
                     this.hookY = j;
                     if (this.hookRestoreState == -1) {
                        switch (b0) {
                           case 0:
                           case 8:
                           case 9:
                           case 47:
                              k = k & -28673 & -513;
                           default:
                              this.hookRestoreState = k;
                        }
                     }

                     if (this.hookPullSteps < 0) {
                        this.hookedObjectState = k & -8 | 4;
                     } else {
                        this.hookedObjectState = k & -8 | 2;
                     }
                     break;
                  case 3:
                  case 10:
                  case 12:
                  case 13:
                  case 15:
                  case 16:
                  case 17:
                  case 18:
                  case 20:
                  case 21:
                  case 22:
                  case 23:
                  case 24:
                  case 25:
                  case 26:
                  case 27:
                  case 28:
                  case 29:
                  case 30:
                  case 31:
                  case 32:
                  case 33:
                  case 34:
                  case 35:
                  case 36:
                  case 37:
                  case 38:
                  case 39:
                  case 40:
                  case 41:
                  case 42:
                  case 44:
                  case 45:
                  case 46:
                  default:
                     if (b0 == -1) {
                        break;
                     }
                     break label63;
               }
            }
         }

         if (flag) {
            for (int k1 = 1; k1 <= 3; k1++) {
               if (objectMap[this.playerX + l * k1][this.playerY] == 32) {
                  objectMap[this.playerX + l * k1][this.playerY] = -1;
               }
            }
         }

         abyte = objectMap[i + l];
         l1 = j;
         b1 = b0;
      } else {
         abyte = objectTimers[i];
         l1 = j;
         b1 = (byte)(abyte[j] - 6);
      }

      abyte[l1] = b1;
      markAreaDirty(i, j);
   }

   private void triggerTile(int var1, int var2) {
      tileMap[var1][var2] = tileMap[var1][var2] & 268435455 | 268435456;
      this.pressActiveSwitch();
   }

   private void updateItemChest(int var1) {
      int i = this.scanX;
      int j = this.scanY;
      int k = tileMap[i][j] >> 8;
      tileMap[i][j] = k << 8 | var1;
      if (objectTimers[i][j] <= 0) {
         if (k == 0) {
            if (this.isPlayerAt(i, j) && this.moveProgress <= 0) {
               this.playerFlags &= -2049;
               tileMap[i][j] = 256 | var1;
               GameCanvas gamecanvas;
               byte b0;
               if (Math.abs(this.lastPickupTime - System.currentTimeMillis()) >= 5000L) {
                  gamecanvas = this;
                  b0 = 40;
               } else {
                  gamecanvas = this;
                  b0 = 48;
               }

               gamecanvas.setPlayerAnim(b0);
               this.playSound(3);
               return;
            }
         } else if ((stateTicks >> 1 & 1) == 0 && sprites[var1 == 14 ? 8 : 22] != null && k < sprites[var1 == 14 ? 8 : 22].getAnimFrameCount(0) - 1) {
            tileMap[i][j] = k + 1 << 8 | var1;
            tileRedrawTimers[i][j] = 24;
         }
      }
   }

   private static boolean isRestingOnSomething(int var0, int var1) {
      int i = var1 - 1;
      int j = var0 - 1;
      int k = var0 + 1;
      return objectTimers[var0][i] <= 6
            && (
               isHeavyObject(var0, i) && ((objectState[var0][i] & 7) == 3 || objectMap[var0][var1] != 16 && objectMap[var0][i] != 1)
                  || objectMap[var0][i] == 46
                  || objectMap[var0][i] == 14
                  || objectMap[var0][i] == 48
            )
         || objectTimers[k][var1] <= 0 && objectMap[k][var1] == 14 && (objectState[k][var1] & 8) != 0 && (objectState[k][var1] & 7) != 3
         || objectTimers[j][var1] <= 0 && objectMap[j][var1] == 14 && (objectState[j][var1] & 8) == 0 && (objectState[j][var1] & 7) != 3;
   }

   private static boolean isHeavyObject(int var0, int var1) {
      if (objectMap[var0][var1] >= 0) {
         switch (objectMap[var0][var1]) {
            case 0:
            case 1:
            case 8:
            case 9:
               return true;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
         }
      }

      return false;
   }

   private static void markAreaDirty(int var0, int var1) {
      int i = var0 - 1;
      int j = var0 + 1;
      int k = var1 - 1;
      int l = var1 + 1;
      tileRedrawTimers[i][k] = 48;
      tileRedrawTimers[var0][k] = 48;
      tileRedrawTimers[j][k] = 48;
      tileRedrawTimers[i][var1] = 48;
      tileRedrawTimers[var0][var1] = 48;
      tileRedrawTimers[j][var1] = 48;
      tileRedrawTimers[i][l] = 48;
      tileRedrawTimers[var0][l] = 48;
      tileRedrawTimers[j][l] = 48;
   }

   private void saveCheckpoint() {
      this.checkpointHasPotion = saveData[10];
      this.checkpointToggleDirection = this.toggleDirection;
      this.checkpointTogglePhase = this.togglePhase;
      this.checkpointPadlockGems = this.padlockGemsRemaining;
      this.checkpointNextCompassTarget = this.nextCompassTarget;
      this.checkpointDiamonds = this.stageDiamonds;
      this.checkpointRedDiamonds = this.stageRedDiamonds;
      this.checkpointX = this.playerX;
      this.checkpointY = this.playerY;
      this.checkpointGoldKeys = this.goldKeys;
      this.checkpointSilverKeys = this.silverKeys;
      this.checkpointEntryWalkSteps = this.entryWalkSteps;
      this.checkpointTreasureCount = this.takenTreasureCount;
      this.checkpointFloodSwitchPresses = this.floodSwitchPresses;
      this.checkpointFloodHeight = this.floodHeight;
      checkpointActiveSwitchId = activeSwitchId;
      if (switchStates != null) {
         System.arraycopy(switchStates, 0, checkpointSwitchStates, 0, switchStates.length);
      }

      for (int i = 0; i < this.mapWidth; i++) {
         System.arraycopy(objectState[i], 0, checkpointObjectState[i], 0, this.mapHeight);
         System.arraycopy(objectTimers[i], 0, checkpointObjectTimers[i], 0, this.mapHeight);
         System.arraycopy(objectMap[i], 0, checkpointObjectMap[i], 0, this.mapHeight);
         System.arraycopy(tileMap[i], 0, checkpointTileMap[i], 0, this.mapHeight);
      }

      if (this.hasEnemies) {
         if (checkpointTileSlots == null) {
            checkpointTileSlots = new int[this.mapWidth][this.mapHeight];
         }

         for (int j = 0; j < this.mapWidth; j++) {
            System.arraycopy(tileSlots[j], 0, checkpointTileSlots[j], 0, this.mapHeight);
         }

         if (checkpointActors == null) {
            checkpointActors = new long[15];
         }

         System.arraycopy(actors, 0, checkpointActors, 0, actors.length);
         if (checkpointEnemies == null) {
            checkpointEnemies = new long[15];
         }

         System.arraycopy(enemies, 0, checkpointEnemies, 0, enemies.length);
         this.checkpointEnemyPhase = this.enemyPhase;
         this.checkpointEnemyChaseTicks = this.enemyChaseTicks;
         this.checkpointEnemyChaseSteps = this.enemyChaseSteps;
         this.checkpointEnemyUnusedS = this.enemyUnusedS;
         this.checkpointEnemyChaseDirection = this.enemyChaseDirection;
         this.checkpointEnemyTriggerX = this.enemyTriggerX;
         this.checkpointEnemyTriggerY = this.enemyTriggerY;
         this.checkpointEnemyTriggerSide = this.enemyTriggerSide;
         this.checkpointEnemyActorSlot = this.enemyActorSlot;
         this.checkpointEnemyTargetRow = this.enemyTargetRow;
         this.checkpointEnemyStepTimer = this.enemyStepTimer;
         checkpointEnemyRemovalPending = this.enemyRemovalPending;
         this.checkpointEnemyRemovalIndex = this.enemyRemovalIndex;
         this.checkpointEnemyCount = this.enemyCount;
      }
   }

   private void restoreCheckpoint() {
      label111: {
         bufferedLeft = -1;
         this.pickupIconSet = this.pickupIconFrame = this.pickupValue = -1;
         this.hookX = 0;
         this.hookY = 0;
         this.cameraPanToCheckpoint = true;
         this.cameraPanX = this.checkpointX * 24 - 160;
         this.cameraPanY = this.checkpointY * 24 - 120 + 31;
         this.fadeHeight = 0;
         int[] aint;
         byte b1;
         if (this.prologueSaw13) {
            this.pendingHint = 15;
            this.prologueSaw13 = false;
            checkpointTileMap[37][7] = -1;
            aint = checkpointTileMap[39];
            b1 = 5;
         } else {
            if (!this.prologueSaw16) {
               break label111;
            }

            this.pendingHint = 17;
            this.prologueSaw16 = false;
            checkpointTileMap[46][7] = -1;
            aint = checkpointTileMap[50];
            b1 = 7;
         }

         aint[b1] = -1;
      }

      this.prologueMarkerHint = false;
      this.idleAnimDelay = 70;
      saveData[10] = this.checkpointHasPotion;
      this.padlockGemsRemaining = this.checkpointPadlockGems;
      this.toggleDirection = this.checkpointToggleDirection;
      this.togglePhase = this.checkpointTogglePhase;
      this.nextCompassTarget = this.checkpointNextCompassTarget;
      this.stageRedDiamonds = this.checkpointRedDiamonds;
      this.stageDiamonds = this.checkpointDiamonds;
      spriteInstances[0].setAnim(2);
      this.playerFlags = 2;
      this.screenShakeTicks = 0;
      this.playerX = this.checkpointX;
      this.playerY = this.checkpointY;
      this.goldKeys = this.checkpointGoldKeys;
      this.silverKeys = this.checkpointSilverKeys;
      this.takenTreasureCount = this.checkpointTreasureCount;
      switch (this.stageKind) {
         case 1:
            this.floodHeight = this.checkpointFloodHeight;
            this.floodSwitchPresses = this.checkpointFloodSwitchPresses;
         case 2:
         default:
            break;
         case 3:
            this.bossHealth = 5;
            break;
         case 4:
            this.bossAnim = 0;
            this.bossPosition = 0;
            this.bossHealth = 3;
            this.bossSlamVisible = false;
            break;
         case 5:
            this.initBossFight();
      }

      activeSwitchId = checkpointActiveSwitchId;
      if (switchStates != null) {
         System.arraycopy(checkpointSwitchStates, 0, switchStates, 0, switchStates.length);
      }

      for (int i = 0; i < this.mapWidth; i++) {
         System.arraycopy(checkpointObjectState[i], 0, objectState[i], 0, this.mapHeight);
         System.arraycopy(checkpointObjectTimers[i], 0, objectTimers[i], 0, this.mapHeight);
         System.arraycopy(checkpointObjectMap[i], 0, objectMap[i], 0, this.mapHeight);
         System.arraycopy(checkpointTileMap[i], 0, tileMap[i], 0, this.mapHeight);
      }

      int j1 = this.mapHeight - 1;

      for (int j = 1; j < j1; j++) {
         int k = this.mapWidth - 1;

         for (int l = 1; l < k; l++) {
            byte b0 = objectMap[l][j];
            int i1 = tileMap[l][j] & 0xFF;
            if (b0 > -1 && b0 < 80 || i1 > -1 && i1 < 80) {
               markAreaDirty(l, j);
            }
         }
      }

      if (spriteInstances[2] != null) {
         spriteInstances[2].setAnim(0);
         objectMap[18][63] = -1;
      }

      this.entryWalkSteps = this.checkpointEntryWalkSteps;
      this.hudDirty = true;
      if (this.hasEnemies) {
         GameCanvas gamecanvas = this;
         this.enemyRemovalPending = checkpointEnemyRemovalPending;
         gamecanvas.enemyRemovalIndex = gamecanvas.checkpointEnemyRemovalIndex;
         gamecanvas.enemyCount = gamecanvas.checkpointEnemyCount;

         for (int k1 = 0; k1 < gamecanvas.mapWidth; k1++) {
            System.arraycopy(checkpointTileSlots[k1], 0, tileSlots[k1], 0, gamecanvas.mapHeight);
         }

         System.arraycopy(checkpointActors, 0, actors, 0, actors.length);
         System.arraycopy(checkpointEnemies, 0, enemies, 0, enemies.length);
         gamecanvas.enemyPhase = gamecanvas.checkpointEnemyPhase;
         gamecanvas.enemyChaseTicks = gamecanvas.checkpointEnemyChaseTicks;
         gamecanvas.enemyChaseSteps = gamecanvas.checkpointEnemyChaseSteps;
         gamecanvas.enemyUnusedS = gamecanvas.checkpointEnemyUnusedS;
         gamecanvas.enemyChaseDirection = gamecanvas.checkpointEnemyChaseDirection;
         gamecanvas.enemyTriggerX = gamecanvas.checkpointEnemyTriggerX;
         gamecanvas.enemyTriggerY = gamecanvas.checkpointEnemyTriggerY;
         gamecanvas.enemyTriggerSide = gamecanvas.checkpointEnemyTriggerSide;
         gamecanvas.enemyActorSlot = gamecanvas.checkpointEnemyActorSlot;
         gamecanvas.enemyTargetRow = gamecanvas.checkpointEnemyTargetRow;
         gamecanvas.enemyStepTimer = gamecanvas.checkpointEnemyStepTimer;
      }

      if (collapsingRows != null) {
         for (int l1 = 0; l1 < this.mapHeight; l1++) {
            for (int i2 = 0; i2 < this.mapWidth; i2++) {
               if (objectMap[i2][l1] == 48) {
                  this.scanX = i2;
                  this.scanY = l1;
                  if ((objectState[i2][l1] & 8) == 0) {
                     this.updateFallingBlock((byte)48);
                  } else {
                     this.trackCollapsingRow();
                  }
               }
            }
         }
      }
   }

   public static void setObject(short var0, short var1, byte var2, int var3) {
      objectMap[var0][var1] = var2;
      objectState[var0][var1] = var3;
   }

   private static boolean within24(int var0, int var1, int var2, int var3) {
      return Math.abs(var0 - var2) < 24 && Math.abs(var1 - var3) < 24;
   }

   private static boolean actorsTouch(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      actorAY = 0;
      actorAX = 0;
      actorBY = 100;
      actorBX = 100;
      if (Math.abs(var0 - var4) <= 1 && Math.abs(var1 - var5) <= 1) {
         actorAX = var0 * 24 + directionOffsets[var2] * var3;
         actorAY = var1 * 24 + directionOffsets[var2 + 8] * var3;
         actorBX = var4 * 24 + directionOffsets[var6] * var7;
         actorBY = var5 * 24 + directionOffsets[var6 + 8] * var7;
         return within24(actorAX, actorAY, actorBX, actorBY);
      } else {
         return false;
      }
   }

   private void updateObjectAtScan() {
      int i = this.scanX;
      int j = this.scanY;
      int k = objectTimers[i][j];
      int l = objectState[i][j];
      byte b0 = objectMap[i][j];
      boolean flag = tileSlots != null && tileSlots[i][j] != 0;
      int j1 = l & 7;
      byte b1;
      byte b2;
      int k2;
      if (flag && tileSlots[i][j] != 3) {
         b1 = -1;
         b2 = 1;
         k2 = j - 1;
      } else {
         b1 = 1;
         b2 = 3;
         k2 = j + 1;
      }

      int i1 = k2;
      byte b3 = (this.playerFlags & 4096) == 0 ? this.queuedAction : 0;
      boolean flag1 = actorsTouch(i, j, j1, k, this.playerX, this.playerY, b3, this.moveProgress);
      if (b0 == 1 && flag1) {
         j = j;
         i = i;
         GameCanvas gamecanvas = this;
         this.addEffect(i, j, 3);
         gamecanvas.stageDiamonds++;
         objectMap[i][j] = -1;
         gamecanvas.padlockGemsRemaining--;
         gamecanvas.hookPullSteps = 0;
         if (gamecanvas.padlockGemsRemaining == 0) {
            objectMap[gamecanvas.padlockX][gamecanvas.padlockY] = -1;
         }

         if (objectMap[i][j - 1] == -1) {
            gamecanvas.refreshBackgroundTile(i, j - 1);
         }

         gamecanvas.refreshBackgroundTile(i, j);
         gamecanvas.hudDirty = true;
      } else {
         flag1 = (tileMap[i][j] & 0xFF) == 35;
         if (objectMap[i][i1] == 9 && k <= 0) {
            byte b4 = (byte)tileMap[i][i1];
            int k1;
            if ((k1 = (objectState[i][i1] & 264241152) >> 22) != 34) {
               if (k1 == 33) {
                  if (b4 == -1) {
                     tileMap[i][i1] = 32;
                  }

                  this.playerX = i;
                  this.playerY = i1;
                  objectMap[i][i1] = -1;
                  this.moveProgress = 0;
                  this.escapePressesLeft = 0;
                  this.hurtPlayer(2, 48, 0);
               } else if (objectMap[i][i1] == 19
                  || objectMap[i][i1] == 43
                  || objectMap[i][i1] == 45
                  || objectMap[i][i1] == 46
                  || objectMap[i][i1] == 49
                  || objectMap[i][i1] == 11) {
                  objectMap[i][i1] = -1;
                  this.triggerTile(i, i1);
               }
            }
         }

         if (k <= 0 && !flag1) {
            if (j1 == b2 && this.isPlayerAt(i, i1) && isEmptyFloor(i, i1)) {
               if ((b0 == 0 || b0 == 9) && b1 > 0) {
                  this.hurtPlayer(2, 48, 0);
               } else if (b0 == 1) {
                  objectState[i][i1] = l & -8 | 3;
                  objectTimers[i][i1] = 18;
                  objectMap[i][i1] = 1;
                  objectMap[i][j] = -1;
                  this.refreshBackgroundTile(i, j);
               } else if (b0 == 8) {
                  l &= -4063233;
               }

               objectState[i][j] = l & -8;
            } else if ((isEmptyFloor(i, i1) || objectMap[i][i1] == 21)
               && (!this.isPlayerAt(i, j) || this.escapePressesLeft > 0)
               && (
                  !this.isPlayerAt(i, i1) && !within24(actorAX, actorAY, actorBX, actorBY - 1)
                     || this.escapePressesLeft > 0
                     || this.deathTimer != 0
                     || b0 != 0 && j1 == b2
               )) {
               if (b1 <= 0 && (tileSlots == null || tileSlots[i][i1] == 0)) {
                  l &= -4063240;
                  objectState[i][j] = l;
                  objectTimers[i][i1] = 0;
               } else {
                  l = l + 131072 & -8 | b2;
                  objectState[i][i1] = l | -2147483648;
                  objectTimers[i][i1] = 18;
                  objectMap[i][i1] = b0;
                  objectMap[i][j] = -1;
                  markAreaDirty(i, j);
                  tileRedrawTimers[i][j + b1 * 2] = 24;
                  this.refreshBackgroundTile(i, j);
               }
            } else if (isHeavyObject(i, i1)) {
               if (b1 < 0 && (tileSlots == null || tileSlots[i][i1] == 0) && objectMap[i][j + 1] < 0) {
                  objectState[i][j + 1] = l & -8 | 3;
                  objectState[i][j + 1] = objectState[i][j + 1] | -2147483648;
                  objectMap[i][j + 1] = b0;
                  objectTimers[i][j + 1] = 18;
                  objectMap[i][j] = -1;
               } else if (objectTimers[i][i1] <= 0) {
                  if ((l & 4063232) >> 17 >= 2) {
                     if (b0 == 8) {
                        objectMap[i][j] = 54;
                        objectState[i][j] = 0;
                        markAreaDirty(i, j);
                        return;
                     }

                     if (objectMap[i][i1] == 8) {
                        objectMap[i][i1] = 54;
                        objectState[i][i1] = 0;
                        markAreaDirty(i, i1);
                        return;
                     }
                  }

                  label472: {
                     l &= -4063233;
                     short short1;
                     if (isEmptyFloor(i - 1, j) && isEmptyFloor(i - 1, i1) && !this.isPlayerAt(i - 1, j)) {
                        objectTimers[i][j] = (byte)(((l & 28672) >> 12) + 1);
                        tileRedrawTimers[i][j] = 24;
                        k2 = (l & -8 | 4) & -3073;
                        short1 = 2048;
                     } else {
                        if (!isEmptyFloor(i + 1, j) || !isEmptyFloor(i + 1, i1) || this.isPlayerAt(i + 1, j)) {
                           break label472;
                        }

                        objectTimers[i][j] = (byte)(((l & 28672) >> 12) + 1);
                        tileRedrawTimers[i][j] = 24;
                        k2 = (l & -8 | 2) & -3073;
                        short1 = 1024;
                     }

                     l = k2 | short1 | 512;
                  }

                  objectState[i][j] = l;
               }
            } else if (b0 == 8) {
               if ((l & 4063232) >> 17 >= 2) {
                  objectMap[i][j] = 54;
                  objectState[i][j] = 0;
                  markAreaDirty(i, j);
                  return;
               }

               l &= -4063233;
            } else {
               l = l & -3073 & -4063233 & -8;
               objectState[i][j] = l;
            }
         } else if (!flag1) {
            if ((l & 512) == 0) {
               if (j1 == 3 && (tileMap[i][j] & 0xFF) == 6 && k <= 12) {
                  k = (byte)(k - (stateTicks & 1));
                  tileRedrawTimers[i][j] = 24;
               } else {
                  k = (byte)(k - 6);
               }

               if (k == 0 || k == 12) {
                  label317: {
                     int l2;
                     switch (l & 3072) {
                        case 1024:
                           k2 = l & -57;
                           l2 = l + 8;
                           break;
                        case 2048:
                           k2 = l & -57;
                           l2 = l - 8;
                           break;
                        default:
                           break label317;
                     }

                     l = k2 | l2 & 56;
                  }

                  if (k == 0) {
                     if ((tileMap[i][j] & 0xFF) == 6) {
                        l &= -449;
                     }

                     if (j1 == b2) {
                        if ((b0 == 0 || b0 == 9) && b1 > 0 && !isEmptyFloor(i, j + 1)) {
                           vibrate(200);
                           this.playSound(14);
                           this.screenShakeTicks = 10;
                           if (b0 == 9 && this.escapePressesLeft > 0 && this.isPlayerAt(i, j)) {
                              this.hurtPlayer(1, 0, 0);
                              this.hudDirty = true;
                           }

                           if (!isHeavyObject(i, j + 1)) {
                              l = l & -449 | 64;
                           }
                        }

                        tileRedrawTimers[i][j] = 30;
                        if (!this.isPlayerAt(i, i1)) {
                           l &= -8;
                        }
                     }
                  }
               }

               objectTimers[i][j] = (byte)k;
               objectState[i][j] = l;
            } else {
               byte b5;
               byte b6;
               label299: {
                  b5 = 0;
                  b6 = 0;
                  byte b8;
                  if (j1 == 4) {
                     b8 = -1;
                  } else {
                     if (j1 != 2) {
                        break label299;
                     }

                     b8 = 1;
                  }

                  b5 = b8;
               }

               if (isEmptyFloor(i, i1) && !this.isPlayerAt(i, i1)) {
                  if ((k = (byte)(k - 6)) <= 0) {
                     k = (byte)0;
                     l = l & -513 & -8;
                  }

                  objectTimers[i][j] = (byte)k;
                  objectState[i][j] = l;
                  tileRedrawTimers[i][j] = 24;
               } else if (isEmptyFloor(i + b5, j)
                  && !this.isPlayerAt(i + b5, j)
                  && isEmptyFloor(i + b5, i1)
                  && !this.isPlayerAt(i + b5, i1)
                  && (objectState[i][i1] & 512) == 0) {
                  if (k >= 6 || (stateTicks & 3) == 0) {
                     k++;
                  }

                  if (k >= 12) {
                     l &= -513;
                     if (b5 != 0) {
                        k = (byte)6;
                        objectMap[i][j] = -1;
                        if (isEmptyFloor(i + b5, i1)) {
                           k = (byte)12;
                           l = l & -8 | b2;
                           b6 = b1;
                        }
                     } else {
                        l &= -8;
                        k = (byte)0;
                     }

                     objectState[i + b5][j + b6] = l | -2147483648;
                     objectTimers[i + b5][j + b6] = (byte)k;
                     objectMap[i + b5][j + b6] = b0;
                     markAreaDirty(i, j);
                     tileRedrawTimers[i][j + b1 * 2] = 24;
                  } else {
                     objectTimers[i][j] = (byte)k;
                     objectState[i][j] = l;
                     tileRedrawTimers[i][j] = 24;
                  }
               } else {
                  if ((k = (byte)(k - 6)) <= 0) {
                     k = (byte)0;
                     l = l & -513 & -8;
                  }

                  objectTimers[i][j] = (byte)k;
                  objectState[i][j] = l;
                  tileRedrawTimers[i][j] = 24;
                  this.redrawTile(i, j);
               }
            }
         }

         int j2 = l & 536870912;
         byte b7 = objectTimers[i][j];
         k = l & 1073741824;
         if (j2 == 0 && b7 != 0 || k == 0 && flag) {
            this.refreshBackgroundTile(i, j);
         }

         if (j2 != 0 && b7 == 0 || k != 0 && !flag) {
            this.redrawTile(i, j);
         }

         int l1 = (l & 512) != 0
            ? 1
            : (
               b7 != 0 || j2 != 0
                  ? 2
                  : (
                     flag
                        ? 3
                        : (
                           tileMap[i][j] > -1 && tileMap[i][j] < 38
                              ? 4
                              : (
                                 (isEmptyFloor(i - 1, j) || isEmptyFloor(i + 1, j))
                                       && isHeavyObject(i, j + 1)
                                       && (objectState[i][j + 1] & 7) == 0
                                       && i != this.hookX
                                       && j != this.hookY
                                    ? 6
                                    : 0
                              )
                        )
                  )
            );
         l = ((l & -536870913 | (b7 != 0 ? 536870912 : 0)) & -1073741825 | (flag ? 1073741824 : 0)) & 2147483647 | (l1 != 0 ? Integer.MIN_VALUE : 0);
         objectState[i][j] = l;
         int i2;
         if ((i2 = ((objectState[i][j] & 448) >> 6) - 1) >= 0 && i2 < 5) {
            objectState[i][j] = objectState[i][j] & -449 | objectState[i][j] + 64 & 448;
         }
      }
   }

   private static Sprite loadSprite(String var0, int var1, int var2, int var3) {
      Sprite sprite = null;

      try {
         sprite = new Sprite();
         byte[] abyte = readPackEntry(var0, var1);
         sprite.load(abyte, 0);

         for (int i = var2; i <= var3; i++) {
            sprite.buildModuleImages(i, 0, -1, -1);
         }

         sprite.currentPalette = var2;
         sprite.moduleData = null;
         System.gc();
      } catch (Exception exception) {
      }

      return sprite;
   }

   private static Sprite loadSprite(String var0, int var1) {
      boolean flag = false;
      var1 = var1;
      return loadSprite(var0, var1, 0, 0);
   }

   private static Sprite loadSpriteDefaultPalette(String var0, int var1, int var2) {
      return loadSprite(var0, var1, 0, 0);
   }

   private static Image[] loadModuleImages(String var0, int var1, int var2) {
      Sprite sprite = null;

      try {
         sprite = new Sprite();
         byte[] abyte = readPackEntry(var0, var1);
         sprite.load(abyte, 0);
         sprite.buildModuleImages(var2, 0, -1, -1);
         unloadSprite(sprite, false);
         System.gc();
      } catch (Exception exception) {
      }

      return sprite.moduleImages[var2];
   }

   private static Image loadImage(String var0, int var1) {
      Image image = null;

      try {
         byte[] abyte;
         image = Image.createImage(abyte = readPackEntry(var0, var1), 0, abyte.length);
         System.gc();
      } catch (Exception exception) {
      }

      return image;
   }

   public static byte[] readPackEntry(String var0, int var1) {
      byte[] abyte = null;
      InputStream inputstream = var0.getClass().getResourceAsStream(var0);

      try {
         abyte = new byte[inputstream.read() << 3];
         inputstream.read(abyte);
         int i = readInt(abyte, var1 << 3);
         var1 = readInt(abyte, (var1 << 3) + 4);
         inputstream.skip(i);
         abyte = new byte[var1];
         inputstream.read(abyte);
         inputstream.close();
      } catch (Exception exception) {
      }

      return abyte;
   }

   private static int measureFirstLine(FontRenderer var0, String var1, int var2) {
      if (var2 != -1 && (var2 = var1.indexOf(10)) != -1) {
         var1 = var1.substring(0, var2);
      }

      if ((var2 = var1.indexOf(125)) != -1) {
         var1 = var1.substring(0, var2);
      }

      var0.measure(var1);
      return FontRenderer.measuredWidth;
   }

   private static int readShort(InputStream var0) throws IOException {
      return var0.read() & 0xFF | (var0.read() & 0xFF) << 8;
   }

   private static int keyBit(int var0) {
      if (var0 < 0) {
         if (var0 == -10) {
            return var0;
         }

         var0 = Math.abs(var0);
      }

      switch (var0) {
         case 1:
            return 1;
         case 2:
            return 2;
         case 3:
            return 4;
         case 4:
            return 8;
         case 5:
            return 16;
         case 6:
            return 32;
         case 7:
            return 64;
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 99:
         case 100:
         case 101:
         case 105:
         case 107:
         case 108:
         case 111:
         case 112:
         case 113:
         case 115:
         case 119:
         case 120:
         default:
            return 0;
         case 23:
            return 128;
         case 35:
         case 106:
            return 512;
         case 42:
         case 117:
            return 256;
         case 48:
         case 109:
            return 1024;
         case 49:
         case 114:
            return 2048;
         case 50:
         case 116:
            return 4096;
         case 51:
         case 121:
            return 8192;
         case 52:
         case 102:
            return 16384;
         case 53:
         case 103:
            return 32768;
         case 54:
         case 104:
            return 65536;
         case 55:
         case 118:
            return 131072;
         case 56:
         case 98:
            return 262144;
         case 57:
         case 110:
            return 524288;
      }
   }

   private void resetEnemies() {
      if (this.hasEnemies) {
         actors = new long[15];
         checkpointActors = new long[15];
         enemies = new long[15];
         checkpointEnemies = new long[15];
         this.enemyPhase = 3;
         this.enemyRemovalPending = true;
         this.enemyChaseTicks = -1;
         this.enemyChaseSteps = 0;
         this.enemyUnusedS = 0;
         this.enemyChaseDirection = 0;
         this.enemyTriggerX = 0;
         this.enemyTriggerY = 0;
         this.enemyTriggerSide = 0;
         this.enemyActorSlot = 0;
         this.enemyTargetRow = 0;
         this.enemyStepTimer = -1;
         this.enemyRemovalIndex = 0;
         this.enemyCount = 0;
      }
   }

   private byte spawnEnemy(byte var1, byte var2, byte var3, byte var4) {
      byte b1 = 0;

      while (true) {
         byte b0 = b1;
         if (b1 >= 15 || enemyBits(b0, (byte)0, (byte)4) == 0) {
            if (b0 < 15) {
               setEnemyBits(b0, (byte)1, (byte)0, (byte)4);
               setEnemyBits(b0, var4, (byte)4, (byte)2);
               setEnemyBits(b0, var1, (byte)6, (byte)7);
               setEnemyBits(b0, var1, (byte)27, (byte)7);
               setEnemyBits(b0, var2, (byte)13, (byte)7);
               setEnemyBits(b0, var3, (byte)20, (byte)7);
               return b0;
            } else {
               return -1;
            }
         }

         b1 = (byte)(b0 + 1);
      }
   }

   private static int slotBits(int var0, byte var1, byte var2, byte var3) {
      return var0 >>> var1 * 9 + var2 & ~(-1 << var3);
   }

   private static void setSlotBits(int var0, int var1, byte var2, byte var3, byte var4, byte var5) {
      // The bytecode reuses the byte parameter slots for full int values; plain int locals do the same.
      int i = tileSlots[var0][var1];
      int previous = slotBits(i, var2, var4, var5);
      int shift = (byte)(var2 * 9 + var4);
      previous <<= shift;
      i ^= previous;
      int value = var3 << shift;
      i |= value;
      tileSlots[var0][var1] = i;
      tileRedrawTimers[var0][var1] = 24;
      tileRedrawTimers[var0][var1 + 1] = 24;
   }

   private static int actorBits(int var0, byte var1, byte var2) {
      return (int)(actors[var0 - 1] >>> var1 & ~(-1L << var2));
   }

   private static void setActorBits(int var0, byte var1, byte var2, byte var3) {
      long i = actorBits(var0, var2, var3);
      long j = actors[--var0];
      i <<= var2;
      j ^= i;
      long k = (long)var1 << var2;
      j |= k;
      actors[var0] = j;
   }

   private static int enemyBits(byte var0, byte var1, byte var2) {
      return (int)(enemies[var0] >>> var1 & ~(-1L << var2));
   }

   private static void setEnemyBits(byte var0, byte var1, byte var2, byte var3) {
      long i = enemyBits(var0, var2, var3);
      long j = enemies[var0];
      i <<= var2;
      j ^= i;
      int shifted = var1 << var2;   // an int shift in the original; narrowing it to byte corrupts the enemy
      j |= shifted;
      enemies[var0] = j;
   }

   private static byte findEnemyAt(int var0, int var1) {
      byte b1 = 0;

      while (true) {
         byte b0 = b1;
         if (b1 >= 15) {
            return -1;
         }

         if (var0 == enemyBits(b0, (byte)13, (byte)7) && var1 == enemyBits(b0, (byte)20, (byte)7)) {
            return b0;
         }

         b1 = (byte)(b0 + 1);
      }
   }

   private void updateEnemies() {
      if (this.hasEnemies) {
         label728:
         if (this.enemyChaseTicks >= 0 && this.enemyStepTimer >= 0) {
            byte b37;
            byte b48;
            byte b49;
            byte b50;
            this.enemyChaseTicks++;
            GameCanvas gamecanvas = this;
            int i = actorBits(this.enemyActorSlot, (byte)14, (byte)6);
            int j = actorBits(gamecanvas.enemyActorSlot, (byte)20, (byte)6);
            label724:
            switch (actorBits(gamecanvas.enemyActorSlot, (byte)47, (byte)2)) {
               case 0:
                  setActorBits(gamecanvas.enemyActorSlot, (byte)1, (byte)47, (byte)2);
                  byte b11 = (byte)actorBits(gamecanvas.enemyActorSlot, (byte)49, (byte)5);
                  byte b13 = (byte)actorBits(gamecanvas.enemyActorSlot, (byte)45, (byte)2);
                  int l5 = 0;
                  if (b13 != 0) {
                     if (b13 == 2) {
                        while (
                           i >= gamecanvas.enemyTriggerX
                              && (byte)(objectMap[i + 1][j] - 80) < 0
                              && objectMap[i + 1][j] != 10
                              && objectMap[i + 1][j] != 37
                              && objectMap[i + 1][j] != 34
                              && objectMap[i + 1][j] != 35
                        ) {
                           i++;
                        }
                     } else {
                        while (
                           i <= gamecanvas.enemyTriggerX
                              && (byte)(objectMap[i + 1][j] - 80) < 0
                              && objectMap[i + 1][j] != 10
                              && objectMap[i + 1][j] != 37
                              && objectMap[i + 1][j] != 34
                              && objectMap[i + 1][j] != 35
                        ) {
                           i++;
                        }
                     }
                  } else if ((byte)(objectMap[i + 1][j] - 80) < 0
                     && objectMap[i + 1][j] != 10
                     && objectMap[i + 1][j] != 37
                     && objectMap[i + 1][j] != 34
                     && objectMap[i + 1][j] != 35) {
                     while (
                        (byte)(objectMap[i + 1][j] - 80) < 0
                           && objectMap[i + 1][j] != 10
                           && objectMap[i + 1][j] != 37
                           && objectMap[i + 1][j] != 34
                           && objectMap[i + 1][j] != 35
                     ) {
                        i++;
                     }
                  } else {
                     while (l5 < b11) {
                        l5++;
                        if ((byte)(objectMap[i][j] - 80) < 0
                           && objectMap[i][j] != 10
                           && objectMap[i][j] != 37
                           && objectMap[i][j] != 34
                           && objectMap[i][j] != 35) {
                           l5 = b11;
                        } else {
                           i--;
                        }
                     }
                  }

                  if (gamecanvas.enemyTriggerSide != 2 && i <= gamecanvas.enemyTriggerX) {
                     while (
                        (byte)(objectMap[i + 1][j] - 80) < 0
                           && objectMap[i + 1][j] != 10
                           && objectMap[i + 1][j] != 37
                           && objectMap[i + 1][j] != 34
                           && objectMap[i + 1][j] != 35
                     ) {
                        i++;
                     }
                  }

                  setActorBits(gamecanvas.enemyActorSlot, (byte)i, (byte)14, (byte)6);
                  b37 = 1;

                  while (true) {
                     byte b18 = b37;
                     if ((byte)(objectMap[i - b18][j] - 80) >= 0
                        || objectMap[i - b18][j] == 10
                        || objectMap[i - b18][j] == 37
                        || objectMap[i - b18][j] == 34
                        || objectMap[i - b18][j] == 35) {
                        b37 = gamecanvas.enemyActorSlot;
                        b48 = b18;
                        b49 = 49;
                        b50 = 5;
                        break label724;
                     }

                     b37 = (byte)(b18 + 1);
                  }
               case 1:
                  if (j == gamecanvas.enemyTargetRow) {
                     gamecanvas.enemyChaseTicks = -1;
                     gamecanvas.enemyStepTimer = -1;
                     clearActorSlot(gamecanvas.enemyActorSlot);
                     gamecanvas.enemyPhase = 4;
                  } else {
                     byte b1 = (byte)actorBits(gamecanvas.enemyActorSlot, (byte)49, (byte)5);
                     if (gamecanvas.enemyChaseTicks != 0 && gamecanvas.enemyChaseTicks % b1 == 0) {
                        gamecanvas.enemyChaseTicks = 0;
                        byte b2;
                        if ((b2 = (byte)actorBits(gamecanvas.enemyActorSlot, (byte)26, (byte)2)) >= 0) {
                           b37 = 0;

                           while (true) {
                              byte b3 = b37;
                              if (b37 >= b1) {
                                 if (b2 == 2) {
                                    setActorBits(gamecanvas.enemyActorSlot, (byte)0, (byte)47, (byte)2);
                                    setActorBits(gamecanvas.enemyActorSlot, (byte)0, (byte)26, (byte)2);
                                    j++;
                                    b37 = gamecanvas.enemyActorSlot;
                                    b48 = (byte)j;
                                    b49 = 20;
                                    b50 = 6;
                                 } else {
                                    b37 = gamecanvas.enemyActorSlot;
                                    b48 = (byte)(b2 + 1);
                                    b49 = 26;
                                    b50 = 2;
                                 }
                                 break label724;
                              }

                              clearTileSlot(i - b3, j, b2);
                              int l = j;
                              byte b4;
                              if ((b4 = (byte)(b2 + 1)) > 2) {
                                 b4 = 0;
                                 l++;
                              }

                              int i1 = slotBits(tileSlots[i - b3][l], b4, (byte)3, (byte)4);
                              if (l == gamecanvas.enemyTargetRow && b2 == 2 ? i1 != 0 && i1 != 3 : i1 != 12 && i1 != 9) {
                                 setSlotBits(i - b3, l, b4, (byte)7, (byte)3, (byte)4);
                              }

                              b37 = (byte)(b3 + 1);
                           }
                        }
                     }
                  }
               default:
                  break label728;
            }

            setActorBits(b37, b48, b49, b50);
         }

         switch (this.enemyPhase) {
            case 1:
               this.enemyPhase = 2;
               return;
            case 2:
               int l9 = 1;

               while (true) {
                  byte b0 = (byte)l9;
                  if (l9 > 15) {
                     break;
                  }

                  label595:
                  switch (actorBits(b0, (byte)28, (byte)3)) {
                     case 1:
                     case 6:
                     case 7:
                        byte b9 = b0;
                        GameCanvas gamecanvas5 = this;
                        int k3 = actorBits(b9, (byte)31, (byte)6);
                        int j4 = actorBits(b9, (byte)37, (byte)6);
                        byte b12 = (byte)actorBits(b9, (byte)43, (byte)2);
                        byte b15 = (byte)slotBits(tileSlots[k3][j4], b12, (byte)7, (byte)2);
                        byte b17 = (byte)actorBits(b9, (byte)45, (byte)2);
                        boolean flag2 = (byte)actorBits(b9, (byte)28, (byte)3) == 7;
                        byte b22 = 0;
                        if (b17 > 1) {
                           b17 = -1;
                        }

                        if (b15 == 0) {
                           byte b24 = (byte)(b12 + 1);
                           int k7 = j4;
                           if (b12 == 2) {
                              b24 = 0;
                              k7 = j4 + 1;
                           }

                           if (slotBits(tileSlots[k3][k7], b24, (byte)3, (byte)4) == 7) {
                              gamecanvas5.enemyChaseSteps = 1;
                              if (flag2) {
                                 clearActorSlot(b9);
                                 gamecanvas5.enemyPhase = 3;
                                 gamecanvas5.enemyRemovalPending = true;
                              } else {
                                 int l7;
                                 if ((l7 = b24 - 1) < 0) {
                                    l7 = 2;
                                 }

                                 gamecanvas5.enemyStepTimer = 0;
                                 byte b10 = (byte)slotBits(tileSlots[k3][k7], b24, (byte)0, (byte)3);
                                 byte b31 = (byte)actorBits(b9, (byte)54, (byte)3);
                                 setActorBits(b10, b31, (byte)54, (byte)3);
                                 setActorBits(b10, (byte)2, (byte)28, (byte)3);
                                 setActorBits(b10, (byte)0, (byte)47, (byte)2);
                                 setActorBits(b10, (byte)l7, (byte)26, (byte)2);
                                 setActorBits(b10, (byte)k3, (byte)14, (byte)6);
                                 setActorBits(b10, (byte)k7, (byte)20, (byte)6);
                                 setActorBits(b10, (byte)0, (byte)57, (byte)1);
                                 clearActorSlot(b9);
                              }
                              break;
                           }
                        }

                        if (b12 == 2 && b15 == 0) {
                           if ((byte)(objectMap[k3][j4 + 1] - 80) < 0
                              && objectMap[k3][j4 + 1] != 10
                              && objectMap[k3][j4 + 1] != 37
                              && objectMap[k3][j4 + 1] != 34
                              && objectMap[k3][j4 + 1] != 35) {
                              if (b17 != 0) {
                                 b17 = 0;
                                 setActorBits(b9, (byte)0, (byte)45, (byte)2);
                              }
                           } else {
                              label582: {
                                 GameCanvas gamecanvas7;
                                 byte b39;
                                 int l10;
                                 int i11;
                                 byte b46;
                                 byte b47;
                                 byte b53;
                                 if (b17 == 0) {
                                    if (flag2) {
                                       clearTileSlot(k3, j4, b12);
                                    } else {
                                       setSlotBits(k3, j4, b12, (byte)15, (byte)3, (byte)4);
                                    }

                                    if ((b22 = gamecanvas5.stepActor(b9, k3, j4, b17, b12, (byte)1, flag2)) >= 0) {
                                       if ((byte)(objectMap[k3 + -1][j4] - 80) < 0
                                          && objectMap[k3 + -1][j4] != 10
                                          && objectMap[k3 + -1][j4] != 37
                                          && objectMap[k3 + -1][j4] != 34
                                          && objectMap[k3 + -1][j4] != 35) {
                                          gamecanvas5.spawnActor(k3 - 1, j4, (byte)(flag2 ? 7 : 5), (byte)-1, (byte)2, (byte)actorBits(b9, (byte)54, (byte)3));
                                       }
                                       break label582;
                                    }

                                    gamecanvas7 = gamecanvas5;
                                    b39 = b9;
                                    l10 = k3;
                                    i11 = j4;
                                    b46 = b17;
                                    b47 = b12;
                                    b53 = -1;
                                 } else {
                                    gamecanvas7 = gamecanvas5;
                                    b39 = b9;
                                    l10 = k3;
                                    i11 = j4;
                                    b46 = b17;
                                    b47 = b12;
                                    b53 = b17;
                                 }

                                 b22 = gamecanvas7.stepActor(b39, l10, i11, b46, b47, b53, flag2);
                              }

                              if (b22 == -2) {
                                 byte b40;
                                 byte b41;
                                 byte b44;
                                 label569: {
                                    int j7;
                                    int j8;
                                    if ((j8 = j7 = objectState[k3][j4 + 1]) != -1 && byteBits(j8, (byte)6, (byte)1) == 1) {
                                       byte b33 = b17;
                                       int k8 = j4;
                                       j8 = k3;
                                       boolean flag3 = true;
                                       boolean flag5 = true;

                                       label565:
                                       while (true) {
                                          boolean flag4 = flag5;

                                          while (flag4) {
                                             flag4 = true;
                                             j8 -= b33;
                                             if (b33 == 0) {
                                                if ((byte)(objectMap[j8 - 1][k8] - 80) >= 0
                                                   || objectMap[j8 - 1][k8] == 10
                                                   || objectMap[j8 - 1][k8] == 37
                                                   || objectMap[j8 - 1][k8] == 34
                                                   || objectMap[j8 - 1][k8] == 35 && (byte)(objectMap[j8 + 1][k8] - 80) >= 0
                                                   || objectMap[j8 + 1][k8] == 10
                                                   || objectMap[j8 + 1][k8] == 37
                                                   || objectMap[j8 + 1][k8] == 34
                                                   || objectMap[j8 + 1][k8] == 35) {
                                                   flag3 = true;
                                                   flag5 = false;
                                                   continue label565;
                                                }
                                             } else {
                                                if ((byte)(objectMap[j8][k8 + 1] - 80) < 0
                                                   && objectMap[j8][k8 + 1] != 10
                                                   && objectMap[j8][k8 + 1] != 37
                                                   && objectMap[j8][k8 + 1] != 34
                                                   && objectMap[j8][k8 + 1] != 35) {
                                                   flag4 = false;
                                                }

                                                if ((byte)(objectMap[j8][k8] - 80) >= 0
                                                   || objectMap[j8][k8] == 10
                                                   || objectMap[j8][k8] == 37
                                                   || objectMap[j8][k8] == 34
                                                   || objectMap[j8][k8] == 35) {
                                                   flag3 = flag4;
                                                   flag5 = false;
                                                   continue label565;
                                                }

                                                if (!flag4) {
                                                   flag5 = flag4;
                                                   break label565;
                                                }
                                             }
                                          }

                                          flag5 = flag3;
                                          break;
                                       }

                                       if (flag5) {
                                          gamecanvas5.enemyChaseSteps = 0;
                                          gamecanvas5.enemyStepTimer = 0;
                                          setActorBits(b9, (byte)2, (byte)28, (byte)3);
                                          setActorBits(b9, byteBits(j7, (byte)0, (byte)6), (byte)0, (byte)7);
                                          setActorBits(b9, (byte)2, (byte)26, (byte)2);
                                          setActorBits(b9, (byte)k3, (byte)14, (byte)6);
                                          l9 = b9;
                                          b40 = (byte)(j4 + 1);
                                          b41 = 20;
                                          b44 = 6;
                                          break label569;
                                       }
                                    }

                                    l9 = b9;
                                    b40 = 3;
                                    b41 = 28;
                                    b44 = 3;
                                 }

                                 setActorBits(l9, b40, b41, b44);
                                 k3 += b17;
                              } else {
                                 b17 = b22;
                                 k3 += b17;
                              }
                           }
                        }

                        if (b22 != -2) {
                           switch (b17) {
                              case -1:
                              case 1:
                                 byte b36 = b15;
                                 byte b34 = b12;
                                 int j9 = j4;
                                 int l8 = k3;
                                 if (++b36 > 2) {
                                    b36 = 0;
                                 }

                                 setSlotBits(l8, j9, b34, b36, (byte)7, (byte)2);
                                 break label595;
                              case 0:
                                 boolean flag1 = flag2;
                                 byte b35 = b12;
                                 int k9 = j4;
                                 int i9 = k3;
                                 byte b32 = b9;
                                 if (!flag1 && slotBits(tileSlots[i9][k9], b35, (byte)3, (byte)4) == 0) {
                                    setSlotBits(i9, k9, b35, (byte)3, (byte)3, (byte)4);
                                 }

                                 if (++b35 > 2) {
                                    b35 = 0;
                                    setActorBits(b32, (byte)(++k9), (byte)37, (byte)6);
                                 }

                                 setActorBits(b32, b35, (byte)43, (byte)2);
                                 int j10;
                                 byte b42;
                                 byte b45;
                                 if (flag1) {
                                    l9 = i9;
                                    j10 = k9;
                                    b42 = b35;
                                    b45 = 6;
                                 } else {
                                    setSlotBits(i9, k9, b35, b32, (byte)0, (byte)3);
                                    l9 = i9;
                                    j10 = k9;
                                    b42 = b35;
                                    b45 = 0;
                                 }

                                 setSlotBits(l9, j10, b42, b45, (byte)3, (byte)4);
                           }
                        }
                        break;
                     case 2:
                        if (this.enemyStepTimer < 0) {
                           break;
                        }

                        this.enemyStepTimer++;
                        byte b8 = b0;
                        GameCanvas gamecanvas4 = this;
                        int j3 = actorBits(b8, (byte)14, (byte)6);
                        int i4 = actorBits(b8, (byte)20, (byte)6);
                        int i5 = actorBits(b8, (byte)0, (byte)7);
                        int k5 = actorBits(b8, (byte)7, (byte)7);
                        int j6 = actorBits(b8, (byte)47, (byte)2);
                        byte b19;
                        byte b20 = (byte)enemyBits(b19 = (byte)actorBits(b8, (byte)54, (byte)3), (byte)6, (byte)7);
                        switch (j6) {
                           case 0:
                              setActorBits(b8, (byte)1, (byte)47, (byte)2);
                              setActorBits(b8, (byte)(--i4), (byte)20, (byte)6);
                              if ((byte)(objectMap[j3][i4] - 80) < 0
                                 && objectMap[j3][i4] != 10
                                 && objectMap[j3][i4] != 37
                                 && objectMap[j3][i4] != 34
                                 && objectMap[j3][i4] != 35) {
                                 while (
                                    (byte)(objectMap[j3 + 1][i4] - 80) < 0
                                       && objectMap[j3 + 1][i4] != 10
                                       && objectMap[j3 + 1][i4] != 37
                                       && objectMap[j3 + 1][i4] != 34
                                       && objectMap[j3 + 1][i4] != 35
                                 ) {
                                    j3++;
                                 }
                              } else {
                                 while (
                                    (byte)(objectMap[j3][i4] - 80) >= 0
                                       || objectMap[j3][i4] == 10
                                       || objectMap[j3][i4] == 37
                                       || objectMap[j3][i4] == 34
                                       || objectMap[j3][i4] == 35
                                 ) {
                                    j3--;
                                 }
                              }

                              setActorBits(b8, (byte)j3, (byte)14, (byte)6);
                              l9 = (byte)1;

                              while (true) {
                                 byte b26 = (byte)l9;   // l9 only holds (byte) values here
                                 if ((byte)(objectMap[j3 - b26][i4] - 80) >= 0
                                    || objectMap[j3 - b26][i4] == 10
                                    || objectMap[j3 - b26][i4] == 37
                                    || objectMap[j3 - b26][i4] == 34
                                    || objectMap[j3 - b26][i4] == 35) {
                                    setActorBits(b8, b26, (byte)49, (byte)5);
                                    b20 = (byte)(b20 - b26);
                                    byte b28 = 0;
                                    gamecanvas4.enemyChaseDirection = 0;
                                    if (b20 < 0) {
                                       i4 = -b20 * 3;
                                       byte b30 = 0;
                                       int i8;
                                       if ((i8 = b26 + b20) * 3 - b26 != 0 && i8 * 3 <= b26 * 3 / 2) {
                                          b30 = 1;
                                       }

                                       i4 /= b26;
                                       gamecanvas4.enemyChaseDirection = (byte)i4;
                                       gamecanvas4.enemyChaseDirection += b30;
                                       if (gamecanvas4.enemyChaseDirection > 2) {
                                          setActorBits(b8, (byte)1, (byte)57, (byte)1);
                                       }

                                       b28 = (byte)Math.abs(b20);
                                       b20 = 0;
                                    }

                                    if ((k5 = k5 + (b26 - b28)) > i5) {
                                       i4 = i5 - k5;
                                       k5 += i4;
                                       b20 = (byte)(b20 - i4);
                                    }

                                    setActorBits(b8, (byte)k5, (byte)7, (byte)7);
                                    setEnemyBits(b19, b20, (byte)6, (byte)7);
                                    break label595;
                                 }

                                 l9 = (byte)(b26 + 1);
                              }
                           case 1:
                              byte b21 = (byte)actorBits(b8, (byte)49, (byte)5);
                              if (gamecanvas4.enemyStepTimer != 0 && gamecanvas4.enemyStepTimer % b21 == 0) {
                                 gamecanvas4.enemyStepTimer = 0;
                                 byte b23 = (byte)actorBits(b8, (byte)26, (byte)2);
                                 byte b25 = (byte)actorBits(b8, (byte)57, (byte)1);
                                 if (b23 >= 0) {
                                    if (b25 != 1) {
                                       l9 = (byte)0;

                                       while (true) {
                                          byte b27 = (byte)l9;   // l9 only holds (byte) values here
                                          if (l9 >= b21) {
                                             if (gamecanvas4.enemyChaseSteps > 0) {
                                                i4 = i4;
                                                if ((b27 = (byte)(b23 + 1)) > 2) {
                                                   b27 = 0;
                                                   i4++;
                                                }

                                                l9 = (byte)0;

                                                while (true) {
                                                   byte b29 = (byte)l9;   // l9 only holds (byte) values here
                                                   if (l9 >= b21) {
                                                      break;
                                                   }

                                                   if ((byte)(objectMap[j3 - b29][i4] - 80) < 0
                                                      && objectMap[j3 - b29][i4] != 10
                                                      && objectMap[j3 - b29][i4] != 37
                                                      && objectMap[j3 - b29][i4] != 34
                                                      && objectMap[j3 - b29][i4] != 35) {
                                                      setSlotBits(j3 - b29, i4, b27, (byte)8, (byte)3, (byte)4);
                                                   }

                                                   l9 = (byte)(b29 + 1);
                                                }
                                             }

                                             gamecanvas4.enemyChaseSteps++;
                                             break;
                                          }

                                          setSlotBits(j3 - b27, i4, b23, (byte)7, (byte)3, (byte)4);
                                          setActorBits(b8, b23, (byte)43, (byte)2);
                                          setSlotBits(j3 - b27, i4, b23, b8, (byte)0, (byte)3);
                                          l9 = (byte)(b27 + 1);
                                       }
                                    }

                                    if (b25 == 1 || (k5 >= i5 || b20 == 0) && b23 == gamecanvas4.enemyChaseDirection) {
                                       if (b25 == 1) {
                                          setActorBits(b8, (byte)0, (byte)57, (byte)1);
                                       }

                                       setActorBits(b8, (byte)3, (byte)28, (byte)3);
                                       if (b20 == 0) {
                                          setEnemyBits(b19, (byte)3, (byte)0, (byte)4);
                                          setActorBits(b8, (byte)5, (byte)28, (byte)3);
                                          if (gamecanvas4.enemyChaseTicks == -1) {
                                             gamecanvas4.enemyStepTimer = -1;
                                             gamecanvas4.enemyPhase = 4;
                                          }
                                          break label595;
                                       }
                                    }

                                    if (b23 == 0) {
                                       setActorBits(b8, (byte)0, (byte)47, (byte)2);
                                       setActorBits(b8, (byte)2, (byte)26, (byte)2);
                                    } else {
                                       setActorBits(b8, (byte)(b23 - 1), (byte)26, (byte)2);
                                    }
                                 }
                              }
                           default:
                              break label595;
                        }
                     case 3:
                        clearActorSlot(b0);
                     case 4:
                     case 5:
                  }

                  l9 = (byte)(b0 + 1);
               }
            case 3:
            default:
               break;
            case 4:
               GameCanvas gamecanvas3 = this;
               byte b38 = 0;

               while (true) {
                  byte b7 = b38;
                  if (b38 >= 15) {
                     this.enemyPhase = 2;
                     return;
                  }

                  if (enemyBits(b7, (byte)0, (byte)4) == 3) {
                     int i3;
                     int l3;
                     byte b14;
                     byte b16;
                     label380: {
                        label379: {
                           i3 = enemyBits(b7, (byte)13, (byte)7);
                           l3 = enemyBits(b7, (byte)20, (byte)7);
                           int l4 = enemyBits(b7, (byte)4, (byte)2);
                           enemies[b7] = 0L;
                           b14 = 0;
                           b16 = 0;
                           switch (l4) {
                              case 0:
                                 l3++;
                                 b14 = 0;
                                 b38 = 0;
                                 break label379;
                              case 1:
                                 i3++;
                                 b38 = 1;
                                 break;
                              case 2:
                                 i3--;
                                 b38 = -1;
                                 break;
                              default:
                                 break label380;
                           }

                           b14 = b38;
                           b38 = 2;
                        }

                        b16 = b38;
                     }

                     gamecanvas3.spawnActor(i3, l3, (byte)7, b14, b16, findEnemyAt(i3, l3));
                  }

                  b38 = (byte)(b7 + 1);
               }
            case 5:
               int l2 = this.enemyTriggerY;
               int k2 = this.enemyTriggerX;
               GameCanvas gamecanvas2 = this;
               this.enemyChaseTicks = 0;
               int[] aint;
               int i10;
               if (gamecanvas2.enemyTriggerSide > 1) {
                  aint = tileSlots[k2];
                  i10 = l2 - 1;
               } else {
                  aint = tileSlots[k2 + gamecanvas2.enemyTriggerSide];
                  i10 = l2;
               }

               int j5;
               int i6;
               int k6;
               int l6;
               int i7;
               label746: {
                  int k = aint[i10];
                  gamecanvas2.enemyActorSlot = (byte)slotBits(k, (byte)2, (byte)0, (byte)3);
                  setActorBits(gamecanvas2.enemyActorSlot, (byte)0, (byte)47, (byte)2);
                  int k4 = actorBits(gamecanvas2.enemyActorSlot, (byte)7, (byte)7);
                  GameCanvas gamecanvas1;
                  int j1;
                  byte b6;
                  boolean flag;
                  int l1;
                  int i2;
                  switch (gamecanvas2.enemyTriggerSide) {
                     case 2:
                        j5 = 0;
                        i6 = k2;
                        k6 = l2;
                        l6 = k4;
                        i7 = l2 - 1;
                        gamecanvas2.enemyTriggerX = gamecanvas2.enemyTriggerY = -1;
                        break label746;
                     default:
                        j5 = gamecanvas2.enemyTriggerSide;
                        i6 = k2 + j5;
                        j5 = j5 < 0 ? 1 : 2;
                        k6 = l2 + 1;
                        k = k6;
                        j1 = i6;
                        gamecanvas1 = gamecanvas2;
                        b6 = 0;
                        flag = true;
                        l1 = gamecanvas1.enemyTriggerSide;
                        i2 = 0;
                  }

                  label434:
                  while (true) {
                     int j2 = 0;

                     while (
                        tileSlots[j1 + l1][k - 1] != 0 && (byte)(objectMap[j1][k] - 80) >= 0
                           || objectMap[j1][k] == 10
                           || objectMap[j1][k] == 37
                           || objectMap[j1][k] == 34
                           || objectMap[j1][k] == 35
                     ) {
                        j1 += l1;
                     }

                     i10 = 0;

                     while (true) {
                        int k1 = i10;
                        if ((byte)(objectMap[j1 + k1][k] - 80) >= 0
                           || objectMap[j1 + k1][k] == 10
                           || objectMap[j1 + k1][k] == 37
                           || objectMap[j1 + k1][k] == 34
                           || objectMap[j1 + k1][k] == 35) {
                           k1 = Math.abs(k1);
                           i2 += k1;
                           k++;

                           while (tileSlots[j1][k] == 0) {
                              j1 += l1;
                              if (j2 >= k1 || j1 < 0 || j1 == gamecanvas1.mapWidth) {
                                 if (i2 > 0) {
                                    setActorBits(b6, (byte)i2, (byte)7, (byte)7);
                                 }

                                 l6 = k4 - i2;
                                 i7 = l2;
                                 gamecanvas2.enemyTriggerX = k2;
                                 gamecanvas2.enemyTriggerY = l2;
                                 break label434;
                              }

                              j2++;
                           }
                           break;
                        }

                        if (flag) {
                           flag = false;
                           setActorBits(b6 = gamecanvas1.spawnActor(j1, k, (byte)8, (byte)-2, (byte)2, (byte)0), (byte)5, (byte)28, (byte)3);
                           setActorBits(b6, (byte)j1, (byte)14, (byte)6);
                           setActorBits(b6, (byte)k, (byte)20, (byte)6);
                        }

                        setSlotBits(j1 + k1, k, (byte)2, b6, (byte)0, (byte)3);
                        i10 = k1 + l1;
                     }
                  }
               }

               byte b5;
               GameCanvas gamecanvas6;
               label392: {
                  int k10;
                  byte b43;
                  byte b51;
                  byte b52;
                  label391: {
                     setActorBits(gamecanvas2.enemyActorSlot, (byte)j5, (byte)45, (byte)2);
                     gamecanvas2.enemyTargetRow = k6;
                     gamecanvas2.spawnEnemy((byte)l6, (byte)i6, (byte)i7, (byte)j5);
                     setEnemyBits(b5 = findEnemyAt(i6, i7), (byte)2, (byte)0, (byte)4);
                     gamecanvas2.enemyPhase = 1;
                     switch (gamecanvas2.enemyTriggerSide) {
                        case -1:
                           gamecanvas6 = gamecanvas2;
                           i10 = i6 + 1;
                           k10 = l2;
                           b43 = 11;
                           b51 = 1;
                           break;
                        case 0:
                        default:
                           break label392;
                        case 1:
                           gamecanvas6 = gamecanvas2;
                           i10 = i6 - 1;
                           k10 = l2;
                           b43 = 14;
                           b51 = -1;
                           break;
                        case 2:
                           gamecanvas6 = gamecanvas2;
                           i10 = i6;
                           k10 = l2;
                           b43 = 3;
                           b51 = 0;
                           b52 = 0;
                           break label391;
                     }

                     b52 = 2;
                  }

                  b5 = gamecanvas6.spawnActor(i10, k10, b43, b51, b52, b5);
               }

               setActorBits(b5, (byte)6, (byte)28, (byte)3);
               gamecanvas2.enemyTriggerSide = 0;
         }
      }
   }

   private static void clearActorSlot(byte var0) {
      actors[var0 - 1] = 0L;
   }

   private byte stepActor(byte var1, int var2, int var3, byte var4, byte var5, byte var6, boolean var7) {
      byte b0;
      if ((byte)(objectMap[var2 + var6][var3] - 80) < 0
         && objectMap[var2 + var6][var3] != 10
         && objectMap[var2 + var6][var3] != 37
         && objectMap[var2 + var6][var3] != 34
         && objectMap[var2 + var6][var3] != 35) {
         b0 = var6;
         if (var4 != var6) {
            setActorBits(var1, var6 < 0 ? 2 : var6, (byte)45, (byte)2);
         }

         var2 += var6;
         setActorBits(var1, (byte)var2, (byte)31, (byte)6);
         setSlotBits(var2, var3, var5, (byte)var1, (byte)0, (byte)3);
         if (var7) {
            clearTileSlot(var2, var3, var5);
         } else {
            var1 = (byte)actorBits(var1, (byte)28, (byte)3);   // 3-bit field
            int i;
            int b2;
            if (var6 > 0) {
               i = var1 == 6 && this.enemyTriggerX == var2 && this.enemyTriggerY == var3 ? 11 : 4;
               b2 = 9;
            } else {
               i = var1 == 6 && this.enemyTriggerX == var2 && this.enemyTriggerY == var3 ? 14 : 5;
               b2 = 12;
            }

            byte b1 = (byte)b2;
            int j;
            byte b3;
            int k;
            if ((byte)(objectMap[var2][var3 + 1] - 80) < 0
               && objectMap[var2 + var6][var3] != 10
               && objectMap[var2 + var6][var3] != 37
               && objectMap[var2 + var6][var3] != 34
               && objectMap[var2 + var6][var3] != 35) {
               b2 = var2;
               j = var3;
               b3 = var5;
               k = b1;
            } else {
               b2 = var2;
               j = var3;
               b3 = var5;
               k = i;
            }

            setSlotBits(b2, j, b3, (byte)k, (byte)3, (byte)4);
         }
      } else {
         b0 = -2;
      }

      return b0;
   }

   private static byte byteBits(int var0, byte var1, byte var2) {
      return (byte)(var0 >>> var1 & ~(16777215 << var2));
   }

   private static void clearTileSlot(int var0, int var1, byte var2) {
      int i = tileSlots[var0][var1];
      int shift = (byte)(var2 * 9);
      int slot = (i >>> shift & 511) << shift;
      i ^= slot;
      tileSlots[var0][var1] = i;
      tileRedrawTimers[var0][var1] = 24;
   }

   private byte spawnActor(int var1, int var2, byte var3, byte var4, byte var5, byte var6) {
      byte b1 = 1;

      while (true) {
         byte b0 = b1;
         if (b1 > 15 || actorBits(b0, (byte)28, (byte)3) == 0) {
            actors[b0 - 1] = 0L;
            if (var3 == 7) {
               clearTileSlot(var1, var2, var5);
               setActorBits(b0, (byte)7, (byte)28, (byte)3);
            } else {
               setActorBits(b0, (byte)1, (byte)28, (byte)3);
               setActorBits(b0, var6, (byte)54, (byte)3);
               setSlotBits(var1, var2, var5, b0, (byte)0, (byte)3);
               setSlotBits(var1, var2, var5, var3, (byte)3, (byte)4);
            }

            setActorBits(b0, (byte)var1, (byte)31, (byte)6);
            setActorBits(b0, (byte)var2, (byte)37, (byte)6);
            setActorBits(b0, var5, (byte)43, (byte)2);
            if (var4 < 0) {
               var4 = 2;
            }

            setActorBits(b0, var4, (byte)45, (byte)2);
            return b0;
         }

         b1 = (byte)(b0 + 1);
      }
   }

   private void alertCreatures(int var1, int var2) {
      if (this.hasEnemies) {
         this.enemyTriggerSide = tileSlots[var1 - 1][var2] != 0 ? -1 : (tileSlots[var1 + 1][var2] != 0 ? 1 : 0);
         if (this.enemyTriggerSide == 0) {
            this.enemyTriggerSide = tileSlots[var1][var2 - 1] != 0 ? 2 : 0;
         }

         if (this.enemyTriggerSide != 0) {
            this.enemyPhase = 5;
            this.playSound(13);
            this.enemyTriggerX = var1;
            this.enemyTriggerY = var2;
         }
      }
   }

   private void killEnemyAt(int var1, int var2) {
      byte b0 = 2;
      int j = var2;
      int i = var1;
      byte b1;
      if ((b0 = findEnemyAt(i, j)) >= 0) {
         setEnemyBits(b0, (byte)2, (byte)0, (byte)4);
         b1 = 39;
      } else {
         b1 = -1;
      }

      if (b1 > 0) {
         objectMap[var1][var2] = -1;
         j = var2;
         i = var1;
         GameCanvas gamecanvas = this;
         this.enemyPhase = 1;
         gamecanvas.spawnActor(i, j + 1, (byte)0, (byte)0, (byte)0, findEnemyAt(i, j));
      }
   }

   private static int frameHeight(Sprite var0, int var1) {
      var1 = (var1 << 2) + 3;
      return var0.frameRects[var1] & 0xFF;
   }

   private static int frameWidth(Sprite var0, int var1) {
      var1 = (var1 << 2) + 2;
      return var0.frameRects[var1] & 0xFF;
   }

   private static void loadTip(int var0) {
      try {
         var0 %= 8;
         tipText = Text.get(var0 + 69);
         if (var0 <= 4) {
            tipSprite = loadSprite("/tips.f", var0);
         } else {
            tipSprite = null;
         }
      } catch (Exception exception) {
      }
   }

   private void paintTip() {
      this.tipVisible = false;
      loadTip(this.tipIndex);
      Graphics graphics = this.screen;
      this.screen.setColor(798521);
      graphics.fillRoundRect(40, 4, 240, 200, 8, 8);
      graphics.setColor(13540096);
      graphics.drawRoundRect(40, 4, 240, 200, 8, 8);
      int i = 29;
      short short1 = 160;
      String s = tipText;
      graphics = graphics;
      String[] astring = Cutscene.split(s, '\n');
      int j = this.tipIndex % 8;

      for (int k = 0; k < astring.length; k++) {
         fontSmall.font.getHeight();
         int l;
         if ((l = j == 0 && (k == 1 || k == 4) || j > 0 && j < 4 && k == 1 ? (j == 0 && k == 1 ? 7 : (j == 0 && k == 4 ? 6 : 0)) : -1) >= 0) {
            if (tipSprite == null) {
               tipSprite = loadSprite("/tips.f", this.tipIndex);
            }

            if (tipSprite != null) {
               try {
                  int i1 = frameWidth(tipSprite, l);
                  tipSprite.paintFrame(graphics, l, 160 - i1 / 2, i - 5, 0, 0, 0);
                  i += (tipSprite.frameRects[(l << 2) + 3] & 255) + 10;
               } catch (Exception exception) {
               }
            }
         } else {
            i = Cutscene.drawWrappedCentered(graphics, astring[k], 160, i, 210);
         }
      }
   }

   private static void drawMessageBox(Graphics var0, FontRenderer var1, String var2, int var3, int var4, int var5, int var6, boolean var7) {
      var1.unusedSpacing = var6;
      if (var2.endsWith("\n")) {
         var2 = var2.substring(0, var2.length() - 1);
      }

      var2 = insertLineBreaks(var2, 310);
      var5 = measureFirstLine(var1, var2, var7 ? -1 : 0);
      var6 = var1.getTextHeight(var2);
      int x = var3 - (var5 >> 1);   // the bytecode reuses the boolean parameter's slot for this
      var0.setColor(798521);
      var0.fillRoundRect(x - 5, var4 - 5, var5 + 10, var6 + 10, 10, 10);
      var0.setColor(13540096);
      var0.drawRoundRect(x - 5, var4 - 5, var5 + 10, var6 + 10, 10, 10);
      var1.drawLines(var0, var2, var3, var4 + 10, 17);
   }

   private void unloadMapResources() {
      this.unloadMapOnLoad = false;
      unloadSprite(sprites[23], true);
      sprites[23] = null;
      unloadSprite(sprites[24], true);
      sprites[24] = null;
      unloadSprite(sprites[25], true);
      sprites[25] = null;
      unloadSprite(sprites[26], true);
      sprites[26] = null;
      unloadSprite(sprites[17], true);
      sprites[17] = null;
      this.mapPathPoints = null;
      mapPreview = null;
      mapPreviewGraphics = null;
      System.gc();
   }

   private void openWorldMap() {
      this.resetStateTimer();
      this.mapPreviewStale = true;
      this.fullRedraw = true;
      this.mapWalkStarting = true;
      gameState = 21;
      this.loadingStep = 0;
      this.loadingStepCount = 14;
      this.firstSecretStage = 100;
      if (this.stage > getWorldProgress(this.world)) {
         this.stage = getWorldProgress(this.world);
      }

      this.resetMapNodes();
   }

   private void resetMapNodes() {
      mapNodes = new long[12][12];
      this.mapPathPoints = new int[20];
   }

   private static int longBits(long var0, byte var2, byte var3) {
      return (int)(var0 >>> var2 & ~(-1L << var3));
   }

   private static void setMapNodeBits(int var0, int var1, int var2, byte var3, byte var4) {
      long i;
      long j = (long)longBits(i = mapNodes[var0][var1], var3, var4) << var3;
      i ^= j;
      long k = (long)var2 << var3;
      i |= k;
      mapNodes[var0][var1] = i;
   }

   private boolean animateMapUnlock() {
      boolean flag = false;
      int i = this.mapPathLength >> 1;
      GameCanvas gamecanvas;
      int i2;
      if (this.mapPathBlinkDelay < 0) {
         gamecanvas = this;
         i2 = 2;
      } else {
         gamecanvas = this;
         i2 = this.mapPathBlinkDelay - 1;
      }

      gamecanvas.mapPathBlinkDelay = i2;
      if (this.mapTargetX == 0 && this.mapTargetY == 0) {
         this.mapTargetX = this.mapCursorX;
         this.mapTargetY = this.mapCursorY;
      }

      if (this.mapPathBlinkDelay == 0) {
         boolean flag2;
         if (this.mapPathShown == i) {
            flag2 = true;
         } else {
            this.mapPathShown++;
            flag2 = false;
         }

         flag = flag2;
      }

      int j;
      int k;
      byte b0;
      label109: {
         j = longBits(mapNodes[this.mapCursorX][this.mapCursorY], (byte)3, (byte)3);
         k = longBits(mapNodes[this.mapTargetX][this.mapTargetY], (byte)3, (byte)3);
         b0 = 0;
         byte b2;
         switch (j != 1 && k != 1 ? j : 1) {
            case 0:
               b2 = 2;
               break;
            case 1:
               b2 = 8;
               break;
            default:
               break label109;
         }

         b0 = b2;
      }

      int l = 0;
      int i1 = this.mapPathReversed ? i - 1 : 0;

      for (int j1 = this.mapPathReversed ? -1 : 1; l < this.mapPathShown; i1 += j1) {
         int k1 = i1 << 1;
         sprites[17].paintFrame(this.screen, b0, this.mapPathPoints[k1], this.mapPathPoints[k1 + 1], 0, 0, 0);
         l++;
      }

      byte b1;
      label98: {
         b1 = 0;
         b0 = 0;
         byte b3;
         switch (j) {
            case 0:
               b0 = 0;
               b3 = 0;
               break;
            case 1:
               b0 = 2;
               b3 = 9;
               break;
            default:
               break label98;
         }

         b1 = b3;
      }

      j = this.mapCursorX * 13 + b0 + 77;
      i1 = this.mapCursorY * 13 + b0 + 33;
      sprites[17].paintFrame(this.screen, b1, j, i1, 0, 0, 0);
      int l1 = this.mapWalkFacingLeft ? 7 : 6;
      sprites[17].paintFrame(this.screen, l1, j + 6, i1 + 6, 0, 0, 0);
      boolean flag1 = true;
      if (l == i) {
         if (this.mapWalkerBlinkDelay < 0) {
            gamecanvas = this;
            i2 = 2;
         } else {
            gamecanvas = this;
            i2 = this.mapWalkerBlinkDelay - 1;
         }

         gamecanvas.mapWalkerBlinkDelay = i2;
         if (this.mapWalkerBlinkDelay == 0) {
            this.mapWalkerBlinkDelay = 1;
            flag1 = false;
         }
      }

      label92:
      if (!flag1) {
         byte b4;
         switch (k) {
            case 0:
               b0 = 0;
               b4 = 0;
               break;
            case 1:
               b0 = 2;
               b4 = 9;
               break;
            default:
               break label92;
         }

         b1 = b4;
      } else {
         label87: {
            byte b5;
            switch (k) {
               case 0:
                  b0 = 0;
                  b5 = 1;
                  break;
               case 1:
                  b0 = 2;
                  b5 = 5;
                  break;
               default:
                  break label87;
            }

            b1 = b5;
         }
      }

      j = this.mapTargetX * 13 + b0 + 77;
      i1 = this.mapTargetY * 13 + b0 + 33;
      sprites[17].paintFrame(this.screen, b1, j, i1, 0, 0, 0);
      if (!flag1) {
         boolean flag3;
         if (this.mapUnlockFrame == sprites[9].getAnimFrameCount(0)) {
            this.mapUnlockFrame--;
            this.mapUnlockAnimDone = true;
            flag3 = true;
         } else {
            flag3 = false;
         }

         flag = flag3;
         if (!this.stageCompletedBefore) {
            sprites[9].paintFrame(this.screen, this.mapUnlockFrame, j, i1, 0, 0, 0);
         }

         this.mapUnlockFrame++;
      }

      return flag;
   }

   private void paintMapConnection(int var1, int var2) {
      GameCanvas gamecanvas;
      if (!this.cheatSkipExitA) {
         int i = longBits(mapNodes[this.unlockedNodeX][this.unlockedNodeY], (byte)6, (byte)5);
         if ((var2 != unlockedStage || var1 != i) && (var1 != unlockedStage || var2 != i)) {
            return;
         }

         if (var1 == unlockedStage && var2 == i) {
            this.mapPathReversed = true;
         }

         gamecanvas = this;
      } else {
         if ((var2 != unlockedStage || var1 != unlockedStage - 1) && (var1 != unlockedStage || var2 != unlockedStage - 1)) {
            return;
         }

         if (var1 == unlockedStage && var2 == unlockedStage - 1) {
            this.mapPathReversed = true;
         }

         gamecanvas = this;
      }

      gamecanvas.mapPathPoints[this.mapPathLength++] = this.pathX;
      this.mapPathPoints[this.mapPathLength++] = this.pathY;
   }

   private void paintMapNodes() {
      this.mapNodesDrawn = 0;
      if ((this.getStageFlags(this.world, this.stage + 1) & 2) != 0 && this.stage + 1 == unlockedStage || this.stage == unlockedStage) {
         this.mapPathDotsVisible = false;
         this.mapUnlockAnimDone = true;
      }

      for (int i = 0; i < 12; i++) {
         for (int j = 0; j < 12; j++) {
            long k = mapNodes[i][j];
            boolean flag = false;
            if (k != 0L) {
               int i1;
               int j1;
               int k1;
               int l1;
               byte b0;
               byte b1;
               i1 = longBits(k, (byte)3, (byte)3);
               j1 = longBits(k, (byte)6, (byte)5);
               k1 = !this.mapUnlockAnimDone && j1 == unlockedStage && j1 != 0 ? 1 : longBits(k, (byte)0, (byte)3);
               l1 = -1;
               b0 = -1;
               b1 = -1;
               label178:
               if (k1 == 0) {
                  flag = this.getStageDiamonds(this.world, j1) == this.getStageDiamondTotal(this.world, j1);
                  int k5;
                  switch (i1) {
                     case 0:
                        b0 = 17;
                        b1 = 0;
                        k5 = longBits(k, (byte)11, (byte)3) > 2 ? 13 : 0;
                        break;
                     case 1:
                        b0 = 18;
                        b1 = 2;
                        k5 = 9;
                        break;
                     default:
                        break label178;
                  }

                  l1 = k5;
               } else {
                  switch (i1) {
                     case 0:
                        b1 = 0;
                        l1 = 1;
                  }
               }

               int i2 = k1;
               k1 = i1;
               long i3 = k;
               i1 = j;
               int l = i;
               GameCanvas gamecanvas = this;
               int j2 = longBits(i3, (byte)6, (byte)5);
               gamecanvas.mapNodesDrawn |= 1 << j2;
               int k2 = longBits(i3, (byte)11, (byte)3);
               int l2 = 14;
               int j3 = 0;

               while (j3 < k2) {
                  int k3 = 77 + l * 13;
                  int l3 = 33 + i1 * 13;
                  int i4 = longBits(i3, (byte)l2, (byte)4);
                  l2 += 4;
                  int j4 = longBits(i3, (byte)l2, (byte)4);
                  i4 = i4;
                  j4 = j4;
                  long j5;
                  int k4 = longBits(j5 = mapNodes[i4][j4], (byte)6, (byte)5);
                  label150:
                  if ((gamecanvas.mapNodesDrawn & 1 << k4) <= 0) {
                     i4 = 77 + i4 * 13;
                     j4 = 33 + j4 * 13;
                     k3 += 6;
                     l3 += 6;
                     i4 += 6;
                     j4 += 6;
                     gamecanvas.pathX = k3;
                     gamecanvas.pathY = l3;
                     gamecanvas.pathError = 0;
                     gamecanvas.pathDx = i4 - k3;
                     gamecanvas.pathDy = j4 - l3;
                     gamecanvas.pathErrorStepX = 0;
                     gamecanvas.pathErrorStepY = 0;
                     gamecanvas.pathSteps = 1;
                     byte b4 = 1;
                     byte b5 = 1;
                     if (gamecanvas.pathDx < 0) {
                        b4 = -1;
                        gamecanvas.pathDx = -gamecanvas.pathDx;
                     }

                     if (gamecanvas.pathDy < 0) {
                        b5 = -1;
                        gamecanvas.pathDy = -gamecanvas.pathDy;
                     }

                     byte b2 = 0;
                     int l4 = longBits(j5, (byte)0, (byte)3);
                     int i5 = longBits(j5, (byte)3, (byte)3);
                     if (k1 == 1) {
                        i5 = k1;
                     }

                     if (i2 == 1) {
                        if (i5 == 1) {
                           break label150;
                        }

                        l4 = i2;
                     }

                     if (!gamecanvas.mapUnlockAnimDone && k4 == unlockedStage) {
                        l4 = 1;
                     }

                     label161:
                     if (l4 == 0) {
                        byte b6;
                        switch (i5) {
                           case 0:
                              b6 = 2;
                              break;
                           case 1:
                              b6 = 8;
                              break;
                           default:
                              break label161;
                        }

                        b2 = b6;
                     } else {
                        label156: {
                           byte b7;
                           switch (i5) {
                              case 0:
                                 b7 = 3;
                                 break;
                              case 1:
                                 b7 = 4;
                                 break;
                              default:
                                 break label156;
                           }

                           b2 = b7;
                        }
                     }

                     if (gamecanvas.pathDy <= gamecanvas.pathDx) {
                        gamecanvas.pathErrorStepX = gamecanvas.pathDx << 1;
                        gamecanvas.pathErrorStepY = gamecanvas.pathDy << 1;

                        while (true) {
                           if (gamecanvas.pathSteps % 8 == 0) {
                              sprites[17].paintFrame(mapPreviewGraphics, b2, gamecanvas.pathX - 60, gamecanvas.pathY - 51, 0, 0, 0);
                              if (gamecanvas.mapPathDotsVisible) {
                                 gamecanvas.paintMapConnection(j2, k4);
                              }
                           }

                           if (gamecanvas.pathX == i4) {
                              break;
                           }

                           gamecanvas.pathX += b4;
                           gamecanvas.pathError = gamecanvas.pathError + gamecanvas.pathErrorStepY;
                           if (gamecanvas.pathError > gamecanvas.pathDx) {
                              gamecanvas.pathY += b5;
                              gamecanvas.pathError = gamecanvas.pathError - gamecanvas.pathErrorStepX;
                           }

                           gamecanvas.pathSteps++;
                        }
                     } else {
                        gamecanvas.pathErrorStepX = 2 * gamecanvas.pathDy;
                        gamecanvas.pathErrorStepY = 2 * gamecanvas.pathDx;

                        while (true) {
                           if (gamecanvas.pathSteps % 8 == 0) {
                              sprites[17].paintFrame(mapPreviewGraphics, b2, gamecanvas.pathX - 60, gamecanvas.pathY - 51, 0, 0, 0);
                              if (gamecanvas.mapPathDotsVisible) {
                                 gamecanvas.paintMapConnection(j2, k4);
                              }
                           }

                           if (gamecanvas.pathY == j4) {
                              break;
                           }

                           gamecanvas.pathY += b5;
                           gamecanvas.pathError = gamecanvas.pathError + gamecanvas.pathErrorStepY;
                           if (gamecanvas.pathError > gamecanvas.pathDy) {
                              gamecanvas.pathX += b4;
                              gamecanvas.pathError = gamecanvas.pathError - gamecanvas.pathErrorStepX;
                           }

                           gamecanvas.pathSteps++;
                        }
                     }
                  }

                  j3++;
                  l2 += 4;
               }

               if (b1 != -1 && l1 != -1) {
                  if (flag && b0 != -1) {
                     sprites[17].paintFrame(mapPreviewGraphics, b0, i * 13 + b1 + 77 - 60, j * 13 + b1 + 33 - 51, 0, 0, 0);
                  }

                  byte b3;
                  label131: {
                     sprites[17].paintFrame(mapPreviewGraphics, l1, i * 13 + b1 + 77 - 60, j * 13 + b1 + 33 - 51, 0, 0, 0);
                     b3 = -1;
                     byte b8;
                     switch (this.world) {
                        case 0:
                           if (j1 != 8) {
                              break label131;
                           }

                           b8 = 52;
                           break;
                        case 1:
                           if (j1 != 9) {
                              break label131;
                           }

                           b8 = 53;
                           break;
                        case 2:
                           if (j1 == 10) {
                              b8 = 54;
                              break;
                           }
                        default:
                           break label131;
                     }

                     b3 = b8;
                  }

                  if (b3 != -1) {
                     sprites[b3].paintFrame(mapPreviewGraphics, 0, i * 13 + -8 + 77 - 60, j * 13 + -8 + 33 - 51, 0, 0, 0);
                  }
               }
            }
         }
      }

      this.mapPathDotsVisible = false;
   }

   private void loadMapFile(String var1) throws IOException {
      InputStream inputstream;
      int i = ((byte)(inputstream = this.getClass().getResourceAsStream(var1)).read() & 255) + (((byte)inputstream.read() & 255) << 8);
      int j = inputstream.read();
      mapFileData = new byte[i];
      inputstream.read(mapFileData);
      inputstream.close();
      int k = 0;

      for (int l = 0; l < j; l++) {
         byte b0 = mapFileData[k++];
         byte b1 = mapFileData[k++];
         byte b2 = mapFileData[k++];
         byte b3 = mapFileData[k++];
         if (b2 == 1 && b3 < this.firstSecretStage) {
            this.firstSecretStage = b3;
         }

         byte b4 = mapFileData[k++];
         setMapNodeBits(b0, b1, 1, (byte)0, (byte)3);
         setMapNodeBits(b0, b1, b2, (byte)3, (byte)3);
         setMapNodeBits(b0, b1, b3, (byte)6, (byte)5);
         setMapNodeBits(b0, b1, b4, (byte)11, (byte)3);
         b2 = 14;

         for (int i1 = 0; i1 < b4; i1++) {
            byte b5 = mapFileData[k++];
            setMapNodeBits(b0, b1, b5, b2, (byte)4);
            b2 = (byte)(b2 + 4);
            b5 = mapFileData[k++];
            setMapNodeBits(b0, b1, b5, b2, (byte)4);
            b2 = (byte)(b2 + 4);
         }
      }

      mapFileData = null;
      System.gc();
   }

   public static final void drawFramedBox(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      var2 += 15;
      var4 += 2;
      int i = var0.getClipX();
      int j = var0.getClipY();
      int k = var0.getClipWidth();
      int l = var0.getClipHeight();
      var0.setClip(var1, var2, var3, var4);
      var0.setColor(var5);
      var0.fillRect(var1, var2, var3, var4);
      var0.setClip(var1 - 3, var2, var3 + 6, var4);

      for (int i1 = var2; i1 <= var2 + var4; i1 += 8) {
         var0.drawImage(demoUiImages[var6][7], var1, i1, 24);
         var0.drawImage(demoUiImages[var6][5], var1 + var3, i1, 20);
      }

      var0.setClip(var1, var2 - 3, var3, var4 + 6);

      for (int j1 = var1; j1 <= var1 + var3; j1 += 8) {
         var0.drawImage(demoUiImages[var6][4], j1, var2, 36);
         var0.drawImage(demoUiImages[var6][6], j1, var2 + var4, 20);
      }

      var0.setClip(var1 - 3, var2 - 3, var3 + 6, var4 + 6);
      var0.drawImage(demoUiImages[var6][0], var1, var2, 40);
      var0.drawImage(demoUiImages[var6][1], var1 + var3, var2, 36);
      var0.drawImage(demoUiImages[var6][2], var1, var2 + var4, 24);
      var0.drawImage(demoUiImages[var6][3], var1 + var3, var2 + var4, 20);
      var0.setClip(i, j, k, l);
   }

   public static final void drawTitledBox(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      var1 = var0.getClipX();
      var2 = var0.getClipY();
      var3 = var0.getClipWidth();
      var5 = var0.getClipHeight();
      drawFramedBox(var0, 6, 126, 306, var4, 73, 1);
      drawFramedBox(var0, 16, 121 - var8, var7, var8, 73, 1);
      var0.setClip(13, 124, var7 + 6, 3);
      var0.setColor(73);
      var0.fillRect(13, 124, var7 + 6, 3);
      var0.drawImage(demoUiImages[1][3], 16, 124, 24);
      var0.drawImage(demoUiImages[1][2], var7 + 16, 124, 20);
      var0.setClip(var1, var2, var3, var5);
   }

   private void paintShop() {
      if (this.armorLevel != this.shownArmorLevel || this.fullRedraw) {
         if (this.armorLevel != this.shownArmorLevel) {
            this.fullRedraw = true;
         }

         this.screen.setClip(0, 0, 320, 240);
         if (this.fullRedraw) {
            this.drawSealBackground(this.screen, true);
            this.screen.setColor(0);
            this.screen.fillRect(0, 0, 320, Constants.lineHeight);
            this.screen.setColor(16777215);
            this.screen.drawLine(0, Constants.lineHeight, 320, Constants.lineHeight);
            fontMedium.drawString(this.screen, strings[63], 160, 9, 17);
            drawFramedBox(this.screen, 10, 15, 300, 82, 4273165, 0);
            if (this.nextArmorThreshold != -1) {
               fontSmall.drawLines(this.screen, insertLineBreaks(this.shopMessage, 280), 160, 223, 17);
            }

            if (this.shopTextChanged) {
               this.shopTextChanged = false;
               fontSmall.drawString(this.screen, this.shopMessage, 160, 143, 1);
            }

            fontSmall.drawLines(this.screen, this.shopDiamondsText, 160, 208, 17);
            this.drawSelectSoftKey();
            this.drawBackSoftKey();
            this.fullRedraw = false;
         }

         if (this.armorLevel != this.shownArmorLevel) {
            this.screen.setColor(4273165);
            this.screen.fillRect(20, 37 + (this.shownArmorLevel >= 0 ? this.shownArmorLevel : this.armorLevel) * 20 + 2, 7, 9);
         }

         sprites[17].paintFrame(this.screen, 14, 20, 37 + this.armorLevel * 20 + 2, 0, 0, 0);
         drawFramedBox(this.screen, 10, 110, 300, 60, 4273165, 0);
         FontRenderer fontrenderer;
         Graphics graphics;
         String s1;
         if (this.nextArmorThreshold != -1) {
            String s = strings[47] + " " + this.armorThresholdText.toString() + "\n" + strings[27];
            fontrenderer = fontSmall;
            graphics = this.screen;
            s1 = s;
         } else {
            fontrenderer = fontSmall;
            graphics = this.screen;
            s1 = this.shopMessage;
         }

         fontrenderer.drawLines(graphics, s1, 160, 162, 1);
         int j1 = 0;

         for (int i = 0; i < 4; i++) {
            j1 = 37 + i * 20;
            sprites[46].paintFrame(this.screen, i + 0, 27, j1, 0, 0, 0);
            fontSmall.drawString(this.screen, strings[i + 120], 53, j1 + 8, 0);
         }

         Image[] aimage;
         j1 = (aimage = sprites[0].moduleImages[0])[11].getWidth();
         int j = aimage[15].getWidth();
         this.screen.drawImage(aimage[11], 140, 127, 0);

         for (int k = 0; k < 8; k++) {
            int l = k >= 4 ? 13 : 15;
            this.screen.drawImage(aimage[l], j1 + 140, 127, 0);
            j1 += j;
         }

         int k1 = this.armorLevel;
         int l1 = j1;
         j1 -= j << 2;

         for (int i1 = 0; i1 <= k1; i1++) {
            this.screen.drawImage(aimage[15], j1 + 140, 127, 0);
            j1 += j;
         }

         this.screen.drawImage(aimage[17], l1 + 140, 127, 0);
         if (this.armorLevel != this.shownArmorLevel) {
            this.shownArmorLevel = this.armorLevel;
         }
      }
   }

   private void playSound(int var1) {
      this.sound.play(var1);
   }

   private void paintCredits() {
      if (this.subState == 2) {
         Graphics graphics = this.screen;
         this.screen.setColor(0, 0, 0);
         graphics.fillRect(0, 0, 320, 240);
         int i = -17;
         int j = this.creditsFirstLine;
         int k = this.creditsFirstLine;
         int l = this.creditsScroll >= 272 ? -(this.creditsScroll % 17) : 272 - this.creditsScroll - 17;

         while (j < creditsText.length && i < 272) {
            if (creditsText[j] == 10) {
               FontRenderer.setColor(16777215);
               int i5 = j - k;
               int j5 = i + l;
               short short1 = 314;
               boolean flag2 = false;
               Object object = null;
               int k1 = j5;
               int j1 = i5;
               int i1 = k;
               FontRenderer fontrenderer = fontSmall;
               byte[] abyte = creditsText;
               Graphics graphics1 = graphics;
               int l1 = fontrenderer.font.getHeight();
               int i2 = 0;
               int j2 = k1;
               boolean flag = k1 != -1;
               int k2 = FontRenderer.textColor;
               int l2 = i1;
               boolean flag1 = true;
               int i3 = i1;
               int j3 = 0;

               for (int k3 = i1; k3 <= i1 + j1; k3++) {
                  int l3 = 10;
                  if (k3 < i1 + j1) {
                     l3 = abyte[k3] & 255;
                  }

                  if (flag1 && i2 > 314) {
                     i2 = j3;
                     abyte[i3] = 10;
                     k3 = i3 - 1;
                  } else if (l3 == 10) {
                     if (flag1 && flag) {
                        i2 = 160 - (i2 >> 1);
                        k3 = l2 - 1;
                     } else {
                        i2 = 0;
                        l2 = k3 + 1;
                        j2 += l1;
                        l1 = fontrenderer.font.getHeight();
                     }

                     if (flag) {
                        flag1 = !flag1;
                     }
                  } else {
                     int j4;
                     int l4;
                     label127: {
                        if (l3 > 32) {
                           j4 = abyte[k3];
                        } else {
                           if (l3 == 32) {
                              i3 = k3;
                              j3 = i2;
                              j4 = i2;
                              l4 = (fontrenderer.unusedInts1[0] & 0xFF) + fontrenderer.unusedInts2[1];
                              break label127;
                           }

                           if (l3 == 1) {
                              FontRenderer.textColor = abyte[++k3];
                              continue;
                           }

                           if (l3 != 2) {
                              continue;
                           }

                           j4 = abyte[++k3] & 255;
                        }

                        l3 = j4;
                        if (j4 >= 89) {
                           if (!flag1 && flag) {
                              fontrenderer.drawString(graphics1, "" + (char)l3, i2, j2 + 10, 0);
                           }

                           j4 = i2;
                        } else {
                           if (!flag1 && flag) {
                              fontrenderer.drawString(graphics1, "" + (char)l3, i2, j2 + 10, 0);
                           }

                           j4 = i2;
                        }

                        l4 = fontrenderer.font.charWidth((char)l3);
                     }

                     i2 = j4 + l4;
                  }
               }

               FontRenderer.textColor = k2;
               int k4 = j2 - k1;
               i += 17;
               k = j + 1;
            }

            j++;
         }

         i = 0;

         for (int i4 = 6; i4 > 0; i4--) {
            graphics.setColor(0);
            graphics.fillRect(0, i, 320, i4);
            graphics.fillRect(0, 240 - i - i4, 320, i4);
            i += i4 + 1;
         }

         this.drawSelectSoftKey();
      }
   }

   private static String[] loadStageNames() {
      String[] astring = new String[16];
      int i = 1;

      for (int j = 0; j < 15; j++) {
         if (j < 11) {
            try {
               String s1;
               if ((s1 = strings[66]).indexOf("%U") != -1 && s1.length() != 1) {
                  astring[j] = Text.replaceAll(s1, "%U", "" + (j + 1));
               } else {
                  astring[j] = s1;
               }
            } catch (Exception exception1) {
               astring[j] = "E";
            }
         } else {
            try {
               String s;
               String[] astring1;
               int k;
               String s2;
               if ((s = strings[62]).indexOf("%U") != -1 && s.length() != 1) {
                  astring1 = astring;
                  k = j;
                  s2 = Text.replaceAll(s, "%U", "" + i);
               } else {
                  astring1 = astring;
                  k = j;
                  s2 = s;
               }

               astring1[k] = s2;
               i++;
            } catch (Exception exception2) {
               astring[j] = "E";
            }
         }
      }

      try {
         astring[15] = strings[31];
      } catch (Exception exception) {
         astring[15] = " ";
      }

      return astring;
   }

   private void showNotice(String var1, int var2, int var3, int var4, int var5, int var6) {
      this.inactivityClock = System.currentTimeMillis();
      noticeMillis = 5000;
      this.noticeX = -1;
      this.noticeY = -1;
      this.noticeText = insertLineBreaks(var1, 300);
      this.noticeColor = var5;
      this.noticeStyle = 0;
   }

   private void dismissNotice(boolean var1) {
      noticeMillis = 0;
      this.fullRedraw = true;
   }

   private static String insertLineBreaks(String var0, int var1) {
      int i = 0;
      int j = 0;
      int k = 0;
      int l = 0;
      StringBuffer stringbuffer = new StringBuffer(var0);

      for (int i1 = 0; i1 <= stringbuffer.length(); i1++) {
         int j1 = 10;
         if (i1 < stringbuffer.length()) {
            j1 = stringbuffer.charAt(i1) & 255;
         }

         if (i > var1 && j != l) {
            i = k;
            stringbuffer.setCharAt(j, '\n');
            i1 = j - 1;
            l = j;
         } else {
            int k1;
            if (j1 == 10) {
               k1 = 0;
            } else {
               if (j1 > 32) {
                  if (j1 == 64) {
                     i += 14;
                  }
                  continue;
               }

               if (j1 != 32) {
                  continue;
               }

               j = i1;
               k = i;
               k1 = i + FontRenderer.getFontSize() + (FontRenderer.getFontSize() << 1);
            }

            i = k1;
         }
      }

      return stringbuffer.toString();
   }

   private static void createFonts() {
      fontMedium = new FontRenderer(0);
      fontSmall = new FontRenderer(8);
   }
}

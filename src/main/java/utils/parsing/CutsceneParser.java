package utils.parsing;

import static utils.HelpMethods.GetDirection;

import java.util.HashMap;

import game_events.*;
import gamestates.Gamestate;
import utils.HelpMethods;

/** A parser which can parse all GeneralEvents. */
public class CutsceneParser {

   private static HashMap<String, EventParser> parseMap = new HashMap<>();

   public static boolean canParseEntry(String entryName) {
      return parseMap.containsKey(entryName);
   }

   public static GeneralEvent parseEvent(String entryName, String[] lineData) {
      return parseMap.get(entryName).parseEvent(lineData);
   }

   static {
      parseMap.put("info", CutsceneParser::parseInfoBox);
      parseMap.put("setGameplayActive", CutsceneParser::parseSetGamePlayActive);
      parseMap.put("levelFinished", CutsceneParser::parseLevelFinished);
      parseMap.put("fadeHeader", CutsceneParser::parseFadeHeader);
      parseMap.put("bigDialogue", CutsceneParser::parseBigDialogue);
      parseMap.put("smallDialogue", CutsceneParser::parseSmallDialogue);
      parseMap.put("fade", CutsceneParser::parseFade);
      parseMap.put("setPlayerVisible", CutsceneParser::parseSetPlayerVisible);
      parseMap.put("fillScreen", CutsceneParser::parseFillScreen);
      parseMap.put("setOverlayImage", CutsceneParser::parseSetOverlayImage);
      parseMap.put("wait", CutsceneParser::parseWait);
      parseMap.put("infoChoice", CutsceneParser::parseInfoChoice);
      parseMap.put("setStartCutscene", CutsceneParser::parseSetStartCutscene);
      parseMap.put("setRequirementMet", CutsceneParser::parseSetRequirementMet);
      parseMap.put("setPlayerSheet", CutsceneParser::parseSetPlayerSheet);
      parseMap.put("playerWalk", CutsceneParser::parsePlayerWalk);
      parseMap.put("npcWalk", CutsceneParser::parseNpcWalk);
      parseMap.put("setDir", CutsceneParser::parseSetDir);
      parseMap.put("addToInventory", CutsceneParser::parseAddToInventory);
      parseMap.put("numberDisplay", CutsceneParser::parseNumberDisplay);
      parseMap.put("addProjectile", CutsceneParser::parseAddProjectile);
      parseMap.put("updateInventory", CutsceneParser::parseUpdateInventory);
      parseMap.put("purchase", CutsceneParser::parsePurchase);
      parseMap.put("goToFlying", CutsceneParser::parseGoToFlying);
      parseMap.put("startSong", CutsceneParser::parseStartSong);
      parseMap.put("startAmbience", CutsceneParser::parseStartAmbience);
      parseMap.put("fadeOutLoops", CutsceneParser::parseFadeOutLoops);
      parseMap.put("stopAllLoops", CutsceneParser::parseStopAllLoops);
      parseMap.put("musicEnabled", CutsceneParser::parseMusicEnabled);
      parseMap.put("playSFX", CutsceneParser::parsePlaySFX);
      parseMap.put("setSprite", CutsceneParser::parseSetSprite);
      parseMap.put("screenShake", CutsceneParser::parseScreenShake);
      parseMap.put("setRedLight", CutsceneParser::parseSetRedLight);
      parseMap.put("fellowShips", CutsceneParser::parseFellowShips);
      parseMap.put("mechanicDisplay", CutsceneParser::parseMechanicDisplay);
      parseMap.put("goToBoss", CutsceneParser::parseGoToBoss);
      parseMap.put("setBossVisible", CutsceneParser::parseSetBossVisible);
      parseMap.put("addObject", CutsceneParser::parseAddObject);
      parseMap.put("moveObject", CutsceneParser::parseMoveObject);
      parseMap.put("clearObjects", CutsceneParser::parseClearObjects);
      parseMap.put("moveCamera", CutsceneParser::parseMoveCamera);
      parseMap.put("reattatchCamera", CutsceneParser::parseReattatchCamera);
      parseMap.put("darken", CutsceneParser::parseDarken);
      parseMap.put("startCinematic", CutsceneParser::parseStartCinematic);
      parseMap.put("exitCinematic", CutsceneParser::parseExitCinematic);
   }

   // ------------------- PARSERS ------------------- //

   private static InfoBoxEvent parseInfoBox(String[] lineData) {
      String text = lineData[1];
      return new InfoBoxEvent(text);
   }

   private static SetGameplayEvent parseSetGamePlayActive(String[] lineData) {
      Boolean active = Boolean.parseBoolean(lineData[1]);
      return new SetGameplayEvent(active);
   }

   private static LevelFinishedEvent parseLevelFinished(String[] lineData) {
      return new LevelFinishedEvent();
   }

   private static FadeHeaderEvent parseFadeHeader(String[] lineData) {
      String inOut = lineData[1];
      Integer yPos = Integer.parseInt(lineData[2]);
      Integer fadeSpeed = Integer.parseInt(lineData[3]);
      String headerText = lineData[4];
      return new FadeHeaderEvent(inOut, yPos, fadeSpeed, headerText);
   }

   private static BigDialogueEvent parseBigDialogue(String[] lineData) {
      String name = lineData[1];
      Integer portraitIndex = Integer.parseInt(lineData[3]);
      Integer speed = Integer.parseInt(lineData[4]);
      String text = lineData[5];
      return new BigDialogueEvent(name, speed, text, portraitIndex);
   }

   private static SmallDialogueEvent parseSmallDialogue(String[] lineData) {
      String name = lineData[1];
      Integer portraitIndex = Integer.parseInt(lineData[3]);
      Integer speed = Integer.parseInt(lineData[4]);
      String text = lineData[5];
      return new SmallDialogueEvent(name, speed, text, portraitIndex);
   }

   private static FadeEvent parseFade(String[] lineData) {
      String direction = lineData[1];
      String color = lineData[2];
      Integer speed = Integer.parseInt(lineData[3]);
      return new FadeEvent(direction, color, speed, false);
   }

   private static SetVisibleEvent parseSetPlayerVisible(String[] lineData) {
      Boolean visible = Boolean.parseBoolean(lineData[1]);
      return new SetVisibleEvent(visible);
   }

   private static WaitEvent parseWait(String[] lineData) {
      Integer waitFrames = Integer.parseInt(lineData[1]);
      return new WaitEvent(waitFrames);
   }

   private static FillScreenEvent parseFillScreen(String[] lineData) {
      String color = lineData[1];
      Boolean active = Boolean.parseBoolean(lineData[2]);
      return new FillScreenEvent(color, active);
   }

   private static SetOverlayImageEvent parseSetOverlayImage(String[] lineData) {
      Boolean active = Boolean.parseBoolean(lineData[1]);
      String fileName = lineData[2];
      // Check for optional arguments scaleWidth and scaleHeight
      if (lineData.length == 5) {
         Float scaleW = Float.parseFloat(lineData[3]);
         Float scaleH = Float.parseFloat(lineData[4]);
         return new SetOverlayImageEvent(active, fileName, scaleW, scaleH);
      } else {
         // Use default scale 3
         return new SetOverlayImageEvent(active, fileName, 3f, 3f);
      }
   }

   private static InfoChoiceEvent parseInfoChoice(String[] lineData) {
      String question = lineData[1];
      String leftChoice = lineData[2];
      String rightChoice = lineData[3];
      return new InfoChoiceEvent(question, leftChoice, rightChoice);
   }

   private static SetStartingCutsceneEvent parseSetStartCutscene(String[] lineData) {
      int triggerObject = HelpMethods.GetTrigger(lineData[1]);
      Integer elementNr = Integer.parseInt(lineData[2]);
      Integer newStartingCutscene = Integer.parseInt(lineData[3]);
      return new SetStartingCutsceneEvent(triggerObject, elementNr, newStartingCutscene);
   }

   private static SetRequirementMetEvent parseSetRequirementMet(String[] lineData) {
      Integer doorIndex = Integer.parseInt(lineData[1]);
      Integer requirementIndex = Integer.parseInt(lineData[2]);
      return new SetRequirementMetEvent(doorIndex, requirementIndex);
   }

   private static SetPlayerSheetEvent parseSetPlayerSheet(String[] lineData) {
      Integer sheetIndex = Integer.parseInt(lineData[1]);
      return new SetPlayerSheetEvent(sheetIndex);
   }

   private static PlayerWalkEvent parsePlayerWalk(String[] lineData) {
      Integer sheetRowIndex = GetDirection(lineData[1]) + 1;
      Float targetX = Float.parseFloat(lineData[2]);
      Float targetY = Float.parseFloat(lineData[3]);
      Integer framesDuration = Integer.parseInt(lineData[4]);
      return new PlayerWalkEvent(sheetRowIndex, targetX, targetY, framesDuration);
   }

   private static NPCWalkEvent parseNpcWalk(String[] lineData) {
      Integer npcIndex = Integer.parseInt(lineData[2]);
      Integer sheetRowIndex = GetDirection(lineData[3]) + 1;
      Float targetX = Float.parseFloat(lineData[4]);
      Float targetY = Float.parseFloat(lineData[5]);
      Integer framesDuration = Integer.parseInt(lineData[6]);
      return new NPCWalkEvent(npcIndex, sheetRowIndex, targetX, targetY, framesDuration);
   }

   private static SetDirEvent parseSetDir(String[] lineData) {
      String entityName = lineData[1];
      int dir = GetDirection(lineData[2]);
      return new SetDirEvent(entityName, dir);
   }

   private static AddToInventoryEvent parseAddToInventory(String[] lineData) {
      String itemName = lineData[1];
      String description = lineData[2];
      String imgFileName = lineData[3];
      return new AddToInventoryEvent(itemName, description, imgFileName);
   }

   private static NumberDisplayEvent parseNumberDisplay(String[] lineData) {
      Integer nr1 = Integer.parseInt(lineData[1]);
      Integer nr2 = Integer.parseInt(lineData[2]);
      Integer nr3 = Integer.parseInt(lineData[3]);
      Integer nr4 = Integer.parseInt(lineData[4]);
      int[] passCode = { nr1, nr2, nr3, nr4 };
      return new NumberDisplayEvent(passCode);
   }

   private static AddProjectileEvent parseAddProjectile(String[] lineData) {
      Integer type = Integer.parseInt(lineData[1]);
      Integer xPos = Integer.parseInt(lineData[2]);
      Integer yPos = Integer.parseInt(lineData[3]);
      Integer xSpeed = Integer.parseInt(lineData[4]);
      Integer ySpeed = Integer.parseInt(lineData[5]);
      return new AddProjectileEvent(type, xPos, yPos, xSpeed, ySpeed);
   }

   private static UpdateInventoryEvent parseUpdateInventory(String[] lineData) {
      String type = lineData[1];
      Integer amount = Integer.parseInt(lineData[2]);
      return new UpdateInventoryEvent(type, amount);
   }

   private static PurchaseEvent parsePurchase(String[] lineData) {
      Integer amount = Integer.parseInt(lineData[1]);
      return new PurchaseEvent(amount);
   }

   private static GoToFlyingEvent parseGoToFlying(String[] lineData) {
      Integer lvl = Integer.parseInt(lineData[1]);
      return new GoToFlyingEvent(lvl);
   }

   private static StartSongEvent parseStartSong(String[] lineData) {
      Integer index = Integer.parseInt(lineData[1]);
      return new StartSongEvent(index);
   }

   private static StartAmbienceEvent parseStartAmbience(String[] lineData) {
      Integer index = Integer.parseInt(lineData[1]);
      return new StartAmbienceEvent(index);
   }

   private static FadeOutLoopEvent parseFadeOutLoops(String[] lineData) {
      return new FadeOutLoopEvent();
   }

   private static StopLoopsEvent parseStopAllLoops(String[] lineData) {
      return new StopLoopsEvent();
   }

   private static MusicEnabledEvent parseMusicEnabled(String[] lineData) {
      Boolean enabled = Boolean.parseBoolean(lineData[1]);
      return new MusicEnabledEvent(enabled);
   }

   private static PlaySFXEvent parsePlaySFX(String[] lineData) {
      Integer index = HelpMethods.GetSFX(lineData[1]);
      return new PlaySFXEvent(index);
   }

   private static SetSpriteEvent parseSetSprite(String[] lineData) {
      String entity = lineData[1];
      Boolean poseActive = Boolean.parseBoolean(lineData[2]);
      Integer colIndex = Integer.parseInt(lineData[3]);
      Integer rowIndex = Integer.parseInt(lineData[4]);
      return new SetSpriteEvent(entity, poseActive, colIndex, rowIndex);
   }

   private static ScreenShakeEvent parseScreenShake(String[] lineData) {
      Integer duration = Integer.parseInt(lineData[1]);
      return new ScreenShakeEvent(duration);
   }

   private static SetRedLightEvent parseSetRedLight(String[] lineData) {
      Boolean active = Boolean.parseBoolean(lineData[1]);
      return new SetRedLightEvent(active);
   }

   private static FellowShipEvent parseFellowShips(String[] lineData) {
      int nrOfShips = (lineData.length - 1) / 3;
      int[] xPos = new int[nrOfShips];
      int[] yPos = new int[nrOfShips];
      int[] takeOffTimer = new int[nrOfShips];
      int index = 0;
      for (int i = 1; i < lineData.length; i += 3) {
         xPos[index] = Integer.parseInt(lineData[i]);
         yPos[index] = Integer.parseInt(lineData[i + 1]);
         takeOffTimer[index] = Integer.parseInt(lineData[i + 2]);
         index++;
      }
      return new FellowShipEvent(xPos, yPos, takeOffTimer);
   }

   private static MechanicDisplayEvent parseMechanicDisplay(String[] lineData) {
      return new MechanicDisplayEvent();
   }

   private static GoToBossEvent parseGoToBoss(String[] lineData) {
      Integer bossNr = Integer.parseInt(lineData[1]);
      return new GoToBossEvent(bossNr);
   }

   private static SetBossVisibleEvent parseSetBossVisible(String[] lineData) {
      Boolean active = Boolean.parseBoolean(lineData[1]);
      return new SetBossVisibleEvent(active);
   }

   private static AddObjectEvent parseAddObject(String[] lineData) {
      String objectName = lineData[1];
      String identifier = lineData[2];
      Float xPos = Float.parseFloat(lineData[3]);
      Float yPos = Float.parseFloat(lineData[4]);
      Float scaleW = Float.parseFloat(lineData[5]);
      Float scaleH = Float.parseFloat(lineData[6]);
      Integer aniSpeed = Integer.parseInt(lineData[7]);
      return new AddObjectEvent(objectName, identifier, xPos, yPos, scaleW, scaleH, aniSpeed);
   }

   private static ObjectMoveEvent parseMoveObject(String[] lineData) {
      String identifier = lineData[1];
      Integer targetX = Integer.parseInt(lineData[2]);
      Integer targetY = Integer.parseInt(lineData[3]);
      Integer duration = Integer.parseInt(lineData[4]);
      return new ObjectMoveEvent(identifier, targetX, targetY, duration);
   }

   private static ClearObjectsEvent parseClearObjects(String[] lineData) {
      return new ClearObjectsEvent();
   }

   private static MoveCameraEvent parseMoveCamera(String[] lineData) {
      Integer deltaX = Integer.parseInt(lineData[1]);
      Integer deltaY = Integer.parseInt(lineData[2]);
      Integer duration = Integer.parseInt(lineData[3]);
      return new MoveCameraEvent(deltaX, deltaY, duration);
   }

   private static ReattatchCameraEvent parseReattatchCamera(String[] lineData) {
      return new ReattatchCameraEvent();
   }

   private static DarkenEvent parseDarken(String[] lineData) {
      Integer alpha = Integer.parseInt(lineData[1]);
      Boolean active = Boolean.parseBoolean(lineData[2]);
      return new DarkenEvent(alpha, active);
   }

   private static StartCinematicEvent parseStartCinematic(String[] lineData) {
      String fileName = lineData[1];
      Gamestate returnGamestate = HelpMethods.ParseGameState(lineData[2]);
      return new StartCinematicEvent(fileName, returnGamestate);
   }

   private static ExitCinematicEvent parseExitCinematic(String[] lineData) {
      return new ExitCinematicEvent();
   }
}

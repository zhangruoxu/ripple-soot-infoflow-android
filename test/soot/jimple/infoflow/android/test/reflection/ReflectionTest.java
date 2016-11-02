package soot.jimple.infoflow.android.test.reflection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Test;

import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NullConstant;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.reflection.ReflectionStat;

public class ReflectionTest {
	
	@Test
	public void TestMalware() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
				"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\malware\\999_30ce7bbd9d97dd81c5856c9ebe8686b2.apk",
				"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
//				"--cgonly",
//				"--android",
//				"--inferencereflmodel",
//				"--metaobjmodel",
//				"--libreflretvalmodel",
//				"--constant-string",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		
		List<Stmt> forNameCalls = new ArrayList<>(); 
		
		for(Edge e : Scene.v().getCallGraph()) {
			if(e.tgt().getSignature().equals("<java.lang.Class: java.lang.Class forName(java.lang.String)>"))
				forNameCalls.add(e.srcStmt());
		}
		
		List<Value> names = forNameCalls.stream()
										.map(s -> {
											if(s instanceof AssignStmt)
												return ((InvokeExpr) ((AssignStmt) s).getRightOp()).getArg(0);
											else
												return ((InvokeStmt) s).getInvokeExpr().getArg(0);
										})
										.collect(Collectors.toList());
		
		for(Stmt s : forNameCalls) {
			Local l = null;
			if(s instanceof AssignStmt)
				l = (Local) ((InvokeExpr) ((AssignStmt) s).getRightOp()).getArg(0);
			else
				l = (Local) ((InvokeStmt) s).getInvokeExpr().getArg(0);
			System.out.println(s);
			queryPTS(l);
			System.out.println("\n");
		}
	}
	
	@Test
	public void TestTest() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
				"C:\\Users\\yifei\\Desktop\\Android project\\apps\\TestDroidRA\\app\\build\\outputs\\apk\\app-debug.apk",
				"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
				"--cgonly",
				"--flowdroid",
				"--inferencereflmodel",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		System.out.println(Scene.v().getCallGraph());
		// SootMethod mainMtd = Scene.v().getMethod("<dummyMainClass: void dummyMainMethod(java.lang.String[])>");
		// SootMethod onCreate = Scene.v().getMethod("<com.example.yifei.testdroidra.MainActivity: void onCreate(android.os.Bundle)>");
		// queryPTSOfVarInMtd(onCreate);
		// System.out.println(onCreate.retrieveActiveBody());
		SootMethod onCreate = Scene.v().getMethod("<com.example.yifei.testdroidra.MainActivity: void foo()>");
		queryPTSOfVarInMtd(onCreate);
	}
	
	@Test
	public void TestDroidbenchRefl4() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\DroidBench-master\\apk\\Reflection\\Reflection4.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		System.out.println(Scene.v().getCallGraph());
		SootMethod mainMtd = Scene.v().getMethod("<de.ecspride.MainActivity: void onCreate(android.os.Bundle)>");
		queryPTSOfVarInMtd(mainMtd);
	}
	
	@Test
	public void TestDroidbenchRefl3() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\DroidBench-master\\apk\\Reflection\\Reflection3.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--flowdroid",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflretvalmodel",
			 "--pathalgo",
			 "contextsensitive",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		SootMethod mainMtd = Scene.v().getMethod("<de.ecspride.MainActivity: void onCreate(android.os.Bundle)>");
		System.out.println("CG: ");
		StreamSupport.stream(Scene.v().getCallGraph().spliterator(), false)
					 .map(Edge::toString)
					 .sorted()
					 .forEach(System.out::println);
		System.out.println(mainMtd.retrieveActiveBody());
		queryPTSOfVarInMtd(mainMtd);
		StreamSupport.stream(Scene.v().getCallGraph().spliterator(), false)
					 .filter(e -> e.src().equals(mainMtd))
					 .forEach(System.out::println);
	}
	
	@Test
	public void TestDroidbenchRefl2() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
				"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\DroidBench-master\\apk\\Reflection\\Reflection2.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		System.out.println(Scene.v().getCallGraph());
		SootMethod mainMtd = Scene.v().getMethod("<de.ecspride.MainActivity: void onCreate(android.os.Bundle)>");
		queryPTSOfVarInMtd(mainMtd);
		System.out.println(mainMtd.retrieveActiveBody());
	}
	
	@Test
	public void TestDroidbenchRefl1() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\DroidBench-master\\apk\\Reflection\\Reflection1.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		System.out.println(Scene.v().getCallGraph());
		SootMethod mainMtd = Scene.v().getMethod("<de.ecspride.MainActivity: void onCreate(android.os.Bundle)>");
		queryPTSOfVarInMtd(mainMtd);
		System.out.println(mainMtd.retrieveActiveBody());
	}
	
	@Test
	public void TestLibraryModeling() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
				"C:\\Users\\yifei\\Desktop\\Android project\\apps\\LibraryModeling\\app\\build\\outputs\\apk\\app-debug.apk",
				"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
				"--android",
				"--inferencereflmodel",
				"--libreflretvalmodel",
				// "--libreflreceivervalmodel",
				"--pathalgo",
				"contextsensitive",
			};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		SootMethod mainMtd = Scene.v().getMethod("<com.example.yifei.librarymodeling.MainActivity: void onCreate(android.os.Bundle)>");
		SootMethod reflMtd = Scene.v().getMethod("<com.example.yifei.librarymodeling.MainActivity: java.lang.Object getTelephonyManager()>");
		queryPTSOfVarInMtd(mainMtd);
		queryPTSOfVarInMtd(reflMtd);
		System.out.println("CG:");
		System.out.println(Scene.v().getCallGraph());
		System.out.println(mainMtd.retrieveActiveBody());
	}
	
	@Test
	public void TestApp1() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\1_com.facebook.orca.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp9() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\9_com.whatsapp.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel",
			"--cgonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp10() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\10_com.ebay.gumtree.au.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp20() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\20_com.telstra.mobile.android.mytelstra.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			// "--libreflretvalmodel",
			"--cgonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		SootMethod mainMtd = Scene.v().getMethod("<com.telstra.mobile.android.mytelstra.widgets.WidgetManager: void startSpinningTheRefreshIndicator(com.telstra.mobile.android.mytelstra.widgets.WidgetConfiguration)>");
		queryPTSOfVarInMtd(mainMtd);
	}
	
	@Test
	public void TestApp28() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\28_com.netmarble.sknightsgb.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
			"--cgonly",
//			 "--conststringonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		SootMethod mtd = Scene.v().getMethod("<net.netmarble.analytics.ReferralReceiver: void sendOtherBroadcastReceiver(android.content.Context,android.content.Intent)>");
		queryPTSOfVarInMtd(mtd);
	}
	@Test
	public void TestApp30() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\30_com.productmadness.hovmobile.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
//			"--conststringonly",
			"--pathalgo",
			"contextsensitive",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp52() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\52_com.facebook.moments.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
//			"--android",
//			"--inferencereflmodel",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp57() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\57_com.nintendo.zaaa.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
//			"--metaobjmodel",
//			"--libreflretvalmodel",
//			"--libreflreceivervalmodel",
//			"--conststringonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp79() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\79_com.jb.gosms.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp91() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\91_me.msqrd.android.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
//			"--metaobjmodel",
//			"--libreflretvalmodel",
//			"--libreflreceivervalmodel",
//			"--conststringonly",
//			"--pathalgo",
//			"contextsensitive",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
//		SootClass clz = Scene.v().getSootClass("ahb");
//		for(SootMethod m : clz.getMethods()) {
//			System.out.println("## " + m.getSignature());
//			System.out.println(m.retrieveActiveBody().toString());
//		}
	}
	
	@Test
	public void TestApp93() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\93_com.igs.fafafa.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp96() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\96_com.bitmango.rolltheballunrollme.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--cgonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp99() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\99_com.spacegame.solitaire.basic.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp108() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\108_com.nordcurrent.canteenhd.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
			"--cgonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp134() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\134_com.ea.game.pvzfree_row.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
//			"--metaobjmodel",
//			"--libreflretvalmodel",
//			"--libreflreceivervalmodel",
			"--conststringonly",
			"--pathalgo",
			"contextsensitive",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		CallGraph cg = Scene.v().getCallGraph();
		for(Edge e : cg) {
			if(e.tgt().getName().equals("startActivity")) {
				System.out.println("## " + e.src());
				System.out.println(e.src().retrieveActiveBody());
			}
		}
	}
	
	@Test
	public void TestApp140() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\140_com.thecarousell.Carousell.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp150() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\150_com.bigduckgames.flow.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
//			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
			
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp157() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\157_com.ea.game.simcitymobile_row.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
//			"--conststringonly",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
//			"--pathalgo",
//			"contextsensitive",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp158() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\158_leagueofmonkeys.torqueburnout.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp178() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\178_com.imangi.templerun.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
//			"--conststringonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp185() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\185_com.rovio.angrybirds.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
//			"--metaobjmodel",
//			"--libreflretvalmodel",
//			"--libreflreceivervalmodel",
			"--conststringonly",
//			 "--cgonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
//		for(SootClass clz : Scene.v().getClasses())
//			for(SootMethod mtd : clz.getMethods())
//				if(mtd.hasActiveBody())
//					for(Unit u : mtd.retrieveActiveBody().getUnits()) {
//						if(u instanceof AssignStmt && ((AssignStmt) u).getLeftOp() instanceof StaticFieldRef) {
//							StaticFieldRef staticRef = ((StaticFieldRef) ((AssignStmt) u).getLeftOp());
//							Value v = ((AssignStmt) u).getRightOp();
//							if(staticRef.getFieldRef().getSignature().equals("<com.unity3d.ads.android.properties.UnityAdsProperties: java.lang.ref.WeakReference CURRENT_ACTIVITY>") &&
//									v instanceof Local) {
//								System.out.println(u);
//								System.out.println("in");
//								System.out.println(mtd.getSignature());
//								System.out.println(v);
//								queryPTS((Local) v);
//							}
//						}
//					}

	}
	
	public void find(CallGraph cg) {
		
	}
	
	@Test
	public void TestApp190() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\190_com.sgn.pandapop.gp.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
//			"--conststringonly",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
			"--cgonly",
//			"--pathalgo",
//			"contextsensitive",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp202() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\202_com.appsorama.kleptocats.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
//			"--android",
//			"--inferencereflmodel",
//			"--metaobjmodel",
//			"--libreflretvalmodel",
//			"--libreflreceivervalmodel",
//			"--libreflretvalmodel",
// 			"--conststringonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp265() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\265_com.ketchapp.twist.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflreceivervalmodel",
			"--libreflretvalmodel",
//			"--conststringonly",
//			 "--pathalgo",
//			 "contextsensitive",
			"--cgonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		SootMethod mtd = Scene.v().getMethod("<com.unity3d.ads.android.UnityAdsDeviceLog: void a(com.unity3d.ads.android.UnityAdsDeviceLog$UnityAdsLogLevel,java.lang.String)>");
		queryPTSOfVarInMtd(mtd);
	}
	
	@Test
	public void TestApp271() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\271_net.HypercraftSarl.BC.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
			"--libreflretvalmodel",
			"--cgonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp277() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\277_com.stupeflix.legend.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
			"--libreflretvalmodel",
//			"--conststringonly",
//			 "--pathalgo",
//			 "contextsensitive",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp281() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\281_air.com.sgn.juicejam.gp.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--android",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
			"--libreflretvalmodel",
//			"--conststringonly",
//			 "--pathalgo",
//			 "contextsensitive",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp286() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\286_le.lenovo.sudoku.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel",
			"--cgonly",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp296() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\296_air.com.tutotoons.app.animalhairsalon2jungle.free.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",
			
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestTestReflection() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\Android project\\apps\\TestReflection\\app\\build\\outputs\\apk\\app-debug.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel",
			"--android",
			// "--flowdroid",
			"--metaobjmodel",
			"--libreflretvalmodel",
			"--libreflreceivervalmodel",		
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		SootMethod mtd = Scene.v().getMethod("<com.example.yifei.testreflection.MainActivity: void onCreate(android.os.Bundle)>");
		System.out.println(mtd.retrieveActiveBody());
		System.out.println(Scene.v().getCallGraph());
	}
	
	public void queryPTSOfVarInMtd(SootMethod mtd) {
		for(Unit u : mtd.retrieveActiveBody().getUnits()) {
			if(u instanceof AssignStmt) {
				AssignStmt assign = (AssignStmt) u;
				if(assign.getRightOp() instanceof StaticInvokeExpr) {
					System.out.println("# Stmt " + assign);
					queryPTS((Local) assign.getLeftOp());
				} else if(assign.getRightOp() instanceof VirtualInvokeExpr) {
					System.out.println("# stmt " + assign);
					VirtualInvokeExpr vInvokeExpr = (VirtualInvokeExpr) assign.getRightOp();
					Local base = (Local) vInvokeExpr.getBase();
					System.out.println("# Base var " + base);
					queryPTS(base);
					Local left = (Local) assign.getLeftOp();
					System.out.println("# Left var " + left);
					queryPTS(left);
				} else {
					System.out.println("# Stmt " + assign);
					Value leftValue = assign.getLeftOp();
					if(leftValue instanceof Local) {
						Local leftVar = (Local) leftValue;
						System.out.println("# Left var " + leftVar);
						queryPTS(leftVar);
					} else if(leftValue instanceof ArrayRef) {
						Local base = (Local) ((ArrayRef) leftValue).getBase();
						System.out.println("# array base: " + base);
						queryPTS(base);
					}
				}
			}if(u instanceof InvokeStmt) {
				System.out.println("# stmt " + u);
				InvokeStmt invokeStmt = (InvokeStmt) u;
				InvokeExpr invokeExpr = invokeStmt.getInvokeExpr();
				if(invokeExpr instanceof VirtualInvokeExpr) {
					VirtualInvokeExpr vInvokeExpr = (VirtualInvokeExpr) invokeExpr;
					Local base = (Local)vInvokeExpr.getBase();
					System.out.println("# base var " + base);
					queryPTS(base);
					if(vInvokeExpr.getArgCount() == 0) {
						continue;
					}
					Value receiver = vInvokeExpr.getArg(0);
					if(receiver instanceof NullConstant) {
						System.out.println("# receiver object is null");
					} else {
						if(receiver instanceof Local) {
							Local receiverLocal = ((Local) receiver);
							System.out.println("# receiver object " + receiverLocal);
							queryPTS(receiverLocal);
						}
					}
					if(vInvokeExpr.getMethod().getName().equals("sendTextMessage")) {
						Local deviceID = (Local) vInvokeExpr.getArg(2);
						System.out.println("3rd argument of sendTextMessage: ");
						System.out.println("# " + deviceID);
						queryPTS(deviceID);
					}
				}
			}
		}
	}
	
	public void queryPTS(Local l) {
		System.out.println("# qurey PTS of " + l);
		PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
		PointsToSet ptsI = pta.reachingObjects(l);
		if (ptsI instanceof PointsToSetInternal) {
			PointsToSetInternal pts = (PointsToSetInternal) ptsI;
			System.out.println("# Var " + l.toString() + " PTS size " + pts.size());
			pts.forall(new P2SetVisitor() {
				@Override
				public void visit(Node n) {
					System.out.println("# Node type: " + n.getClass().getName());
					if(n instanceof AllocNode) {
						AllocNode allocNode = (AllocNode) n;
						System.out.println("\n# AllocNode " + allocNode.toString());
					}
					if(n instanceof StringConstantNode) {
						StringConstantNode scNode = (StringConstantNode) n;
						System.out.println("\n# String constant node " + scNode.getString());
					}
					System.out.println(n);
				}
			});
		} else {
			System.out.println(ptsI.getClass().getName());
		}
	}
}

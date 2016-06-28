package soot.jimple.infoflow.android.test.reflection;

import java.util.stream.StreamSupport;

import org.junit.Test;

import soot.Local;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NullConstant;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;

public class ReflectionTest {
	@Test
	public void TestDroidbenchRefl4() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
				"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\DroidBench-master\\apk\\Reflection\\Reflection4.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms"
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
			"C:\\Users\\yifei\\Desktop\\Android project\\apps\\Reflection3\\app\\build\\outputs\\apk\\app-debug.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel",
			// "--cgonly",
			// "--pathalgo",
			// "contextinsensitive"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		System.out.println(Scene.v().getCallGraph());
		SootMethod mainMtd = Scene.v().getMethod("<de.ecspride.MainActivity: void onCreate(android.os.Bundle)>");
		queryPTSOfVarInMtd(mainMtd);
		System.out.println(mainMtd.retrieveActiveBody());
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
				"--inferencereflmodel",
				
			};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
		SootMethod mainMtd = Scene.v().getMethod("<com.example.yifei.librarymodeling.MainActivity: void onCreate(android.os.Bundle)>");
		queryPTSOfVarInMtd(mainMtd);
		System.out.println(mainMtd.retrieveActiveBody());
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
			// "--inferencereflmodel",
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
			"--inferencereflmodel",
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	@Test
	public void TestApp30() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\30_com.productmadness.hovmobile.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp52() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\52_com.facebook.moments.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp57() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\57_com.nintendo.zaaa.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
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
	public void TestApp157() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\157_com.ea.game.simcitymobile_row.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
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
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp190() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\190_com.sgn.pandapop.gp.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp202() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\202_com.appsorama.kleptocats.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			// "--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
	}
	
	@Test
	public void TestApp271() throws Exception {
		soot.G.reset();
		String[] args = new String[] {
			"C:\\Users\\yifei\\Desktop\\share\\GooglePlayCrawler\\apps\\271_net.HypercraftSarl.BC.apk",
			"C:\\Users\\yifei\\Desktop\\Research\\ICSE17\\libs\\Android\\platforms",
			"--inferencereflmodel"
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
			"--inferencereflmodel"
		};
		soot.jimple.infoflow.android.TestApps.Test.main(args);
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
		PointsToSetInternal pts = (PointsToSetInternal) pta.reachingObjects(l);
		System.out.println("# Var " + l.toString() + " PTS size " + pts.size());
		pts.forall(new P2SetVisitor() {

			@Override
			public void visit(Node n) {
				System.out.println("# Node type: " + n.getClass().getName());
				if(n instanceof StringConstantNode) {
					StringConstantNode scNode = (StringConstantNode) n;
					System.out.println("# String constant node " + scNode.getString());
				}
				System.out.println(n);
			}
		});
		System.out.println();
	}
}

package battlearena.common.world.path;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import battlearena.common.world.TiledWorld;
import battlearena.common.world.path.Node;

public class GamePathFinder
{

        public final TiledWorld map;
        private final com.badlogic.gdx.ai.pfa.PathFinder<Node> pathfinder;
        private final Heuristic<Node> heuristic;
        private final GraphPath<Connection<Node>> connectionPath;

        public GamePathFinder(TiledWorld map)
        {
            this.map = map;
            this.pathfinder = new IndexedAStarPathFinder<Node>(createGraph(map));
            this.connectionPath = new DefaultGraphPath<Connection<Node>>();
            this.heuristic = new Heuristic<Node>()
            {
                @Override
                public float estimate (Node node, Node endNode) {
                    // Manhattan distance
                    return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
                }
            };
        }

        public Node findNextNode(Node source, Node target)
        {

            int sourceX = MathUtils.floor(source.x);
            int sourceY = MathUtils.floor(source.y);
            int targetX = MathUtils.floor(target.x);
            int targetY = MathUtils.floor(target.y);

            if (map == null
                    || sourceX < 0 || sourceX >= map.getWidth()
                    || sourceY < 0 || sourceY >= map.getHeight()
                    || targetX < 0 || targetX >= map.getWidth()
                    || targetY < 0 || targetY >= map.getHeight()) {
                return null;
            }

            Node sourceNode = map.getNodeAt(sourceX, sourceY);
            Node targetNode = map.getNodeAt(targetX, targetY);
            connectionPath.clear();
            pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath);

            return connectionPath.getCount() == 0 ? null : connectionPath.get(0).getToNode();
        }

        private static final int[][] NEIGHBORHOOD = new int[][]
                {
                new int[] {-1,  0},
                new int[] { 0, -1},
                new int[] { 0,  1},
                new int[] { 1,  0}
        };

        public static MyGraph createGraph (TiledWorld map)
        {
            final int height = map.getHeight();
            final int width = map.getWidth();
            MyGraph graph = new MyGraph(map);
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    Node node = map.getNodeAt(x, y);
                    if (node.isWall)
                    {
                        continue;
                    }
                    // Add a connection for each valid neighbor
                    for (int offset = 0; offset < NEIGHBORHOOD.length; offset++)
                    {

                        int neighborX = node.x + NEIGHBORHOOD[offset][0];
                        int neighborY = node.y + NEIGHBORHOOD[offset][1];
                        if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height)
                        {
                            Node neighbor = map.getNodeAt(neighborX, neighborY);
                            if (!neighbor.isWall)
                            {

                                // Add connection to walkable neighbor
                                node.getConnections().add(new DefaultConnection<Node>(node, neighbor));
                            }
                        }
                    }
                    node.getConnections().shuffle();
                }
            }
            return graph;
        }

        private static class MyGraph implements IndexedGraph<Node>
        {

            TiledWorld map;

            public MyGraph (TiledWorld map) {
                this.map = map;
            }

            @Override
            public int getIndex(Node node) {
                return node.getIndex();
            }

            @Override
            public Array<Connection<Node>> getConnections(Node fromNode) {
                return fromNode.getConnections();
            }

            @Override
            public int getNodeCount() {
                return map.getHeight() * map.getHeight();
            }

        }

}

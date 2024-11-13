#include <iostream>

int main() {
    int V, E, C; // Number of vertices, edges, and colors
    int roles, scenes, actors; // Number of roles, scenes, and actors

    // Reading the number of vertices, edges, and colors
    std::cin >> V >> E >> C;

    // Convert to casting problem parameters
    roles = V + 2;
    actors = std::min(roles, C + 2);
    scenes = E + V + 1;

    // Output the number of roles, scenes, and actors
    std::cout << roles << std::endl;
    std::cout << scenes << std::endl;
    std::cout << actors << std::endl;

    // Printing the roles for divas p1 and p2 directly
    std::cout << "1 1" << std::endl;
    std::cout << "1 2" << std::endl;

    // Print remaining roles
    for (int i = 3; i <= roles; ++i) {
        std::cout << actors - 2;
        for (int j = 3; j <= actors; ++j) {
            std::cout << " " << j;
        }
        std::cout << std::endl;
    }

    // Print all the obligatory scenes
    std::cout << "2 2 3" << std::endl;
    for (int i = 3; i <= roles; ++i) {
        std::cout << "2 1 " << i << std::endl;
    }

    // Read edges and print them as scenes
    for (int i = 0; i < E; ++i) {
        int x, y;
        std::cin >> x >> y;
        x += 2;
        y += 2;
        std::cout << "2 " << x << " " << y << std::endl;
    }

    return 0;
}

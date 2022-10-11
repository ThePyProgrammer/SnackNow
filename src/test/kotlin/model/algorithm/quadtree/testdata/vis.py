import matplotlib.pyplot as plt
import numpy as np
import sys


def read_points_data(filename):
    with open(filename, 'r') as f:
        data = f.read().splitlines()
    tmp = [list(map(float, line.split(','))) for line in data]
    n, k = int(tmp[1][0]), int(tmp[1][1])
    bounds, points, queries = tmp[0], tmp[2:2 + n], tmp[2 + n:]
    return bounds, points, queries


def write_points_data(filename, bounds, points, queries):
    with open(filename, 'w') as f:
        f.write(str(bounds[0]) + ',' + str(bounds[1]) + ',' + str(bounds[2]) + ',' + str(bounds[3]))
        f.write('\n')
        f.write(str(len(points)) + ',' + str(len(queries)))
        f.write('\n')
        for p in points:
            f.write(str(p[0]) + ',' + str(p[1]))
            f.write('\n')
        for q in queries:
            f.write(str(q[0]) + ',' + str(q[1]) + ',' + str(q[2]) + ',' + str(q[3]))
            f.write('\n')


def plot_points(bounds, points, queries):
    # plot solid bounding rectangle patch given top left and bottom right coordinates
    plt.gca().add_patch(plt.Rectangle((bounds[0], bounds[1]), bounds[2] - bounds[0], bounds[3] - bounds[1], fill=False))
    plt.scatter(*zip(*points), s=10)

    for i, query in enumerate(queries):
        print('query', i, query)
        plt.gca().add_patch(
            plt.Rectangle((query[0], query[1]), query[2] - query[0], query[3] - query[1], fill=False, color='r'))
        plt.annotate(str(i + 1), (query[0] + 0.0002, query[1] - 0.0005), fontsize=10)

    plt.show()


def generate_grid():
    bounds = [0, 0.01, 0.01, 0]
    n = 20
    points = np.linspace(0, 0.01, n)
    points = [(x, y) for x in points for y in points]
    queries = np.random.uniform(0, 0.01, (10, 4)).tolist()
    for i in range(10):
        a = min(queries[i][0], queries[i][2])
        b = max(queries[i][1], queries[i][3])
        c = max(queries[i][0], queries[i][1])
        d = min(queries[i][1], queries[i][3])
        queries[i] = [a, b, c, d]
    return bounds, points, queries


if __name__ == '__main__':

    if len(sys.argv) > 1:
        bounds, points, queries = read_points_data(sys.argv[1])
        plot_points(bounds, points, queries)
    elif sys.argv[1] == 'generate':
        pass
        # bounds, points, queries = generate_grid()
        # write_points_data("grid.csv",bounds,points,queries)
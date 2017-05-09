import matplotlib.pyplot as plt
import numpy as np
import io


def plot_histogram(np_histogram):
    plt.bar(np_histogram[1][0:-1], np_histogram[0])
    plt.xlabel("Przyrost opoznienia [s]")
    f = io.BytesIO()
    plt.savefig(f)
    plt.close()
    return f
